package org.salex.hmip.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

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
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .baseUrl(config.getApiEndpoint())
                .build();
    }

    public Mono<HmIPState> loadCurrentState() {
        return webClient
                .post()
                .uri("hmip/home/getCurrentState")
                .bodyValue(config.jsonBodyBuilder().getCurrentState())
                .retrieve()
                .bodyToMono(HmIPState.class);
    }

    public Mono<Void> deleteThisClient() {
        return deleteClient(this.config.getClientId());
    }

    public Mono<Void> deleteClient(String clientId) {
        // TODO implement rest call
        return Mono.empty();
    }
}
