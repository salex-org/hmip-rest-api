package org.salex.hmip.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class HmIPCloud {
    private static final Logger LOG = LoggerFactory.getLogger(HmIPCloud.class);

    private final HmIPClient client;
    private final WebClient webClient;

    public HmIPCloud(HmIPClient client) {
        this.client = client;
        this.webClient = WebClient.builder().build();
    }

    private static String prepareSGTINForAPI(String sgtin) {
        return sgtin.replaceAll("[^a-fA-F0-9]", "").toUpperCase();
    }

    private static String createClientAuthToken(String sgtin) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.reset();
            digest.update((sgtin + "jiLpVitHvWnIGD1yo7MA").getBytes("utf-8"));
            return String.format("%0128x", new BigInteger(1, digest.digest())).toUpperCase();
        } catch(NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new HmIPException("Error creating client auth token", e);
        }
    }

    private static Mono<HmIPHosts> getHosts(HmIPClient client) {
        final var url = "https://lookup.homematic.com:48335";
        LOG.info(String.format("Lookup up for the hosts using %s", url));
        var webClient = WebClient.builder().baseUrl(url).build();
        return webClient.post().uri("getHost").bodyValue(client.jsonBodyBuilder().getHosts()).retrieve().bodyToMono(HmIPHosts.class);
    }

    public static Mono<HmIPClient> registerClient(String accessPointSGTIN, String clientName) {
        return registerClient(accessPointSGTIN, clientName, null);
    }

    public static Mono<HmIPClient> registerClient(String accessPointSGTIN, String clientName, String pin) {
        final var client = new HmIPClient();
        client.setApiVersion("12");
        client.setLanguage(Locale.getDefault().toLanguageTag());
        client.setOsType(System.getProperty("os.name"));
        client.setOsVersion(System.getProperty("os.version"));
        client.setAccessPointSGTIN(prepareSGTINForAPI(accessPointSGTIN));
        client.setPin(pin);
        client.setDeviceManufacturer("none");
        client.setDeviceType("Computer");
        client.setDeviceId(UUID.randomUUID().toString());
        client.setClientName(clientName);
        client.setClientVersion("1.0.0");
        client.setClientAuthToken(createClientAuthToken(client.getAccessPointSGTIN()));
        return getHosts(client).flatMap(hosts -> {
            LOG.info(String.format("Using %s for further API calls", hosts.getRestURL()));
            return registerClient(client, hosts);
        });
    }

    private static Mono<HmIPClient> registerClient(HmIPClient client, HmIPHosts hosts) {
        final var webClient = WebClient.builder()
                .defaultHeader("VERSION", client.getApiVersion())
                .defaultHeader("CLIENTAUTH", client.getClientAuthToken())
                .baseUrl(hosts.getRestURL())
                .build();
        LOG.info(String.format("Registering client '%s' on device '%s' for access point with SGTIN '%s'",
                client.getClientName(),
                client.getDeviceId(),
                client.getAccessPointSGTIN()));
        return webClient.post().uri("hmip/auth/connectionRequest").bodyValue(client.jsonBodyBuilder().connectionRequest()).retrieve().toBodilessEntity().
                flatMap(conReqResult -> {
                    LOG.info("Please press the blue button on the access point to acknowledge the client registration...");
                    return webClient
                            .post()
                            .uri("hmip/auth/isRequestAcknowledged")
                            .bodyValue(client.jsonBodyBuilder().isRequestAcknowledged())
                            .retrieve()
                            .toBodilessEntity()
                            .retryWhen(Retry.fixedDelay(12, Duration.ofSeconds(5)))
                            .onErrorResume(error -> {
                                LOG.info("Client registration was not acknowledged");
                                return Mono.error(new HmIPException("Error acknowledging client during registration", error));
                            })
                            .flatMap(isReqAckResult -> {
                                LOG.info("Client registration was acknowledged");
                                LOG.info("Requesting auth token for client");
                                return webClient
                                        .post()
                                        .uri("hmip/auth/requestAuthToken")
                                        .bodyValue(client.jsonBodyBuilder().requestAuthToken())
                                        .retrieve()
                                        .bodyToMono(Map.class)
                                        .onErrorMap(error -> new HmIPException("Error requesting auth token for client", error))
                                        .flatMap(result -> {
                                            if(result.containsKey("authToken")) {
                                                return Mono.just(result.get("authToken").toString());
                                            } else {
                                                return Mono.error(new HmIPException("No auth token received"));
                                            }
                                        })
                                        .flatMap(authToken -> {
                                            LOG.info("Confirming auth token");
                                            return webClient
                                                    .post()
                                                    .uri("hmip/auth/confirmAuthToken")
                                                    .bodyValue(client.jsonBodyBuilder().confirmAuthToken())
                                                    .retrieve()
                                                    .bodyToMono(Map.class)
                                                    .onErrorMap(error -> new HmIPException("Error confirming auth token for client", error))
                                                    .flatMap(result -> {
                                                        if(result.containsKey("clientId")) {
                                                            client.setClientId(result.get("clientId").toString());
                                                            client.setAuthToken(authToken);
                                                            return Mono.just(client);
                                                        } else {
                                                            return Mono.error(new HmIPException("No client ID received"));
                                                        }
                                                    });
                                        });
                            });
                });
    }

    public Mono<Void> deleteThisClient() {
        return deleteClient(this.client.getClientId());
    }

    public Mono<Void> deleteClient(String clientId) {
        return Mono.empty();
    }
}
