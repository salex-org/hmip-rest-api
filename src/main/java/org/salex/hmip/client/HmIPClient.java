package org.salex.hmip.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
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

    public Mono<JsonNode> loadCurrentState() {
        return webClient
                .post()
                .uri("hmip/home/getCurrentState")
                .bodyValue(config.jsonBodyBuilder().getCurrentState())
                .retrieve()
                .bodyToMono(JsonNode.class);
    }
}
