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
import java.util.Properties;
import java.util.UUID;

/**
 * Configuration for a Homematic IP Cloud client.
 * Use {@link Builder} to get an instance.
 * Use {@link #registerClient()} to register a new client in the cloud.
 * Use {@link Builder#properties(Properties)} to load config data for existing client.
 */
public class HmIPConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(HmIPConfiguration.class);
    private static final String LOOKUP_ENDPOINT = "https://lookup.homematic.com:48335";

    private String apiEndpoint;
    private String apiVersion;
    private String language;

    private String osType;
    private String osVersion;

    private String accessPointSGTIN;
    private String pin;

    private String deviceManufacturer;
    private String deviceType;
    private String deviceId;

    private String clientId;
    private String clientName;
    private String clientVersion;
    private String clientAuthToken;

    private String authToken;

    private final JsonBodyBuilder jsonBodyBuilder = new JsonBodyBuilder();

    public JsonBodyBuilder jsonBodyBuilder() {
        return jsonBodyBuilder;
    }

    /**
     * Builds different JSON-Bodies based on the config values.
     */
    public class JsonBodyBuilder {
        private JsonBodyBuilder() {}
        public Map<String, Object> getHosts() {
            return Map.of(
                    "clientCharacteristics", clientCharacteristics(),
                    "id", accessPointSGTIN
            );
        }

        public Map<String, Object> connectionRequest() {
            return Map.of(
                    "deviceId", deviceId,
                    "deviceName", clientName,
                    "sgtin", accessPointSGTIN
            );
        }

        public Map<String, Object> isRequestAcknowledged() {
            return Map.of(
                    "deviceId", deviceId
            );
        }

        public Map<String, Object> requestAuthToken() {
            return Map.of(
                    "deviceId", deviceId
            );
        }

        public Map<String, Object> confirmAuthToken() {
            return Map.of(
                    "deviceId", deviceId,
                    "authToken", authToken
            );
        }

        public Map<String, Object> getCurrentState() {
            return Map.of("clientCharacteristics", clientCharacteristics());
        }

        private Map<String, Object> clientCharacteristics() {
            return Map.of(
                    "apiVersion", apiVersion,
                    "applicationIdentifier", clientName,
                    "applicationVersion", clientVersion,
                    "deviceManufacturer", deviceManufacturer,
                    "deviceType", deviceType,
                    "languange", language,
                    "osType", osType,
                    "osVersion", osVersion
            );
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builds and initializes configurations.
     */
    public static class Builder {
        final private HmIPConfiguration config;

        private Builder() {
            this.config = new HmIPConfiguration();
            config.apiVersion = "12";
            config.language = Locale.getDefault().toLanguageTag();
            config.osType = System.getProperty("os.name");
            config.osVersion = System.getProperty("os.version");
            config.deviceManufacturer = "none";
            config.deviceType = "Computer";
            config.deviceId = UUID.randomUUID().toString();
            config.clientName = "Java Client";
            config.clientVersion = "1.0.0";
        }

        public Builder apiVersion(String apiVersion) {
            config.apiVersion = apiVersion;
            return this;
        }

        public Builder language(String language) {
            config.language = language;
            return this;
        }

        public Builder osType(String osType) {
            config.osType = osType;
            return this;
        }

        public Builder osVersion(String osVersion) {
            config.osVersion = osVersion;
            return this;
        }

        public Builder accessPointSGTIN(String accessPointSGTIN) {
            config.accessPointSGTIN = prepareSGTINForAPI(accessPointSGTIN);
            config.clientAuthToken = createClientAuthToken(config.accessPointSGTIN);
            return this;
        }

        private String prepareSGTINForAPI(String sgtin) {
            return sgtin.replaceAll("[^a-fA-F0-9]", "").toUpperCase();
        }

        private String createClientAuthToken(String sgtin) {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-512");
                digest.reset();
                digest.update((sgtin + "jiLpVitHvWnIGD1yo7MA").getBytes("utf-8"));
                return String.format("%0128x", new BigInteger(1, digest.digest())).toUpperCase();
            } catch(NoSuchAlgorithmException | UnsupportedEncodingException e) {
                throw new HmIPException("Error creating client auth token", e);
            }
        }

        public Builder pin(String pin) {
            config.pin = pin;
            return this;
        }

        public Builder deviceManufacturer(String deviceMAnufacturer) {
            config.deviceManufacturer = deviceMAnufacturer;
            return this;
        }

        public Builder deviceType(String deviceType) {
            config.deviceType = deviceType;
            return this;
        }

        public Builder deviceId(String deviceId) {
            config.deviceId = deviceId;
            return this;
        }

        public Builder clientId(String clientId) {
            config.clientId = clientId;
            return this;
        }

        public Builder clientName(String clientName) {
            config.clientName = clientName;
            return this;
        }

        public Builder clientVersion(String clientVersion) {
            config.clientVersion = clientVersion;
            return this;
        }

        public Builder properties(Properties properties) {
            // TODO Load parameter from properties
            return this;
        }

        public Mono<HmIPConfiguration> build() {
            final var webClient = WebClient.builder()
                    .baseUrl(LOOKUP_ENDPOINT)
                    .build();
            LOG.info(String.format("Lookup up for the hosts using %s", LOOKUP_ENDPOINT));
            return webClient
                    .post()
                    .uri("getHost")
                    .bodyValue(config.jsonBodyBuilder().getHosts())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .onErrorMap(error -> new HmIPException("Error looking up the hosts", error))
                    .flatMap(result -> {
                        if(result.containsKey("urlREST")) {
                            config.apiEndpoint = result.get("urlREST").toString();
                            return Mono.just(config);
                        } else {
                            return Mono.error(new HmIPException("No URL for REST API received"));
                        }
                    });
        }
    }

    public Mono<HmIPConfiguration> registerClient() {
        final var webClient = WebClient.builder()
                .defaultHeader("VERSION", apiVersion)
                .defaultHeader("CLIENTAUTH", clientAuthToken)
                .baseUrl(apiEndpoint)
                .build();
        LOG.info(String.format("Registering client '%s' on device '%s' for access point with SGTIN '%s'", clientName, deviceId, accessPointSGTIN));
        return webClient
                .post()
                .uri("hmip/auth/connectionRequest")
                .bodyValue(jsonBodyBuilder.connectionRequest())
                .retrieve()
                .toBodilessEntity()
                .onErrorMap(error -> new HmIPException("Error registering client", error))
                .map(result -> webClient)
                .flatMap(this::acknowledgeClient);
    }

    private Mono<HmIPConfiguration> acknowledgeClient(WebClient webClient) {
        LOG.info("Please press the blue button on the access point to acknowledge the client registration...");
        return webClient
                .post()
                .uri("hmip/auth/isRequestAcknowledged")
                .bodyValue(jsonBodyBuilder.isRequestAcknowledged())
                .retrieve()
                .toBodilessEntity()
                .retryWhen(Retry.fixedDelay(12, Duration.ofSeconds(5)))
                .onErrorMap(error -> new HmIPException("Error acknowledging client during registration", error))
                .map(result -> webClient)
                .flatMap(this::requestAuthToken);
    }

    private Mono<HmIPConfiguration> requestAuthToken(WebClient webClient) {
        LOG.info("Requesting auth token for client");
        return webClient
                .post()
                .uri("hmip/auth/requestAuthToken")
                .bodyValue(jsonBodyBuilder.requestAuthToken())
                .retrieve()
                .bodyToMono(Map.class)
                .onErrorMap(error -> new HmIPException("Error requesting auth token for client", error))
                .flatMap(result -> {
                    if(result.containsKey("authToken")) {
                        authToken = result.get("authToken").toString();
                        return confirmAuthToken(webClient);
                    } else {
                        return Mono.error(new HmIPException("No auth token received"));
                    }
                });
    }

    private Mono<HmIPConfiguration> confirmAuthToken(WebClient webClient) {
        LOG.info("Confirming auth token");
        return webClient
                .post()
                .uri("hmip/auth/confirmAuthToken")
                .bodyValue(jsonBodyBuilder.confirmAuthToken())
                .retrieve()
                .bodyToMono(Map.class)
                .onErrorMap(error -> new HmIPException("Error confirming auth token for client", error))
                .flatMap(result -> {
                    if(result.containsKey("clientId")) {
                        clientId = result.get("clientId").toString();
                        return Mono.just(this);
                    } else {
                        return Mono.error(new HmIPException("No client ID received"));
                    }
                });
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public String getLanguage() {
        return language;
    }

    public String getOsType() {
        return osType;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public String getAccessPointSGTIN() {
        return accessPointSGTIN;
    }

    public String getPin() {
        return pin;
    }

    public String getDeviceManufacturer() {
        return deviceManufacturer;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public String getClientAuthToken() {
        return clientAuthToken;
    }

    public String getAuthToken() {
        return authToken;
    }
}
