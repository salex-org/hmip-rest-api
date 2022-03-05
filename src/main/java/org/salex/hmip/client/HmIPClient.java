package org.salex.hmip.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Client for the Homematic IP Cloud.
 */
public class HmIPClient {
    private static final Logger LOG = LoggerFactory.getLogger(HmIPClient.class);

    private final HmIPConfiguration config;
    private final WebClient webClient;

    public HmIPClient(HmIPConfiguration config) {
        this.config = config;
        this.webClient = WebClient.builder()
                .defaultHeader("VERSION", config.getApiVersion())
                .defaultHeader("CLIENTAUTH", config.getClientAuthToken())
                .defaultHeader("AUTHTOKEN", config.getAuthToken())
                .baseUrl(config.getApiEndpoint())
                .build();
    }

    public Flux<HmIPState.Client> getClients() {
        return loadCurrentState()
                .map(state -> state.getClients().values())
                .flatMapMany(Flux::fromIterable);
    }

    public Mono<HmIPState.Client> getClient(String clientId) {
        return getClients()
                .filter(client -> client.getId().equals(clientId))
                .next();
    }

    public Mono<HmIPState.Client> getClient() {
        return getClient(config.getClientId());
    }

    public Flux<HmIPState.Device> getDevices() {
        return loadCurrentState()
                .map(state -> state.getDevices().values())
                .flatMapMany(Flux::fromIterable);
    }

    public Mono<HmIPState.Device> getDevice(String deviceId) {
        return getDevices()
                .filter(device -> device.getId().equals(deviceId))
                .next();
    }

    public Mono<HmIPState> loadCurrentState() {
        return webClient
                .post()
                .uri("hmip/home/getCurrentState")
                .bodyValue(config.jsonBodyBuilder().getCurrentState())
                .retrieve()
                .bodyToMono(HmIPState.class);
    }

    public Mono<JsonNode> loadCurrentStateJson() {
        return webClient
                .post()
                .uri("hmip/home/getCurrentState")
                .bodyValue(config.jsonBodyBuilder().getCurrentState())
                .retrieve()
                .bodyToMono(JsonNode.class);
    }
}
