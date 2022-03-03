package org.salex.hmip.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseClientAppConfiguration {
    @Bean
    HmIPClient createHomematicClient(HmIPProperties properties) {
        return HmIPConfiguration.builder()
                .properties(properties)
                .build()
                .map(HmIPClient::new)
                .block();
    }
}
