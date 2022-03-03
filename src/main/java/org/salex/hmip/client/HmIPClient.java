package org.salex.hmip.client;

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
        this.webClient = WebClient.builder().build();
    }

    public Mono<Void> deleteThisClient() {
        return deleteClient(this.config.getClientId());
    }

    public Mono<Void> deleteClient(String clientId) {
        // TODO implement rest call
        return Mono.empty();
    }
}
