package org.salex.hmip.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;

/**
 * Spring Boot command line app to use an already registered client.
 */
@SpringBootApplication
@Profile("client")
@ConfigurationPropertiesScan(basePackageClasses = HmIPProperties.class)
public class UseClientApp implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(UseClientApp.class);

    @Autowired
    private ConfigurableApplicationContext context;

    @Autowired
    private HmIPProperties properties;

    public static void main(String[] args) {
        SpringApplication.run(UseClientApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        HmIPConfiguration.builder()
                .properties(properties)
                .build()
                .map(HmIPClient::new)
                .flatMap(client -> client.loadCurrentState())
                .doOnError(error -> {
                    LOG.error(String.format("Failed to load the current state: %s", error.getMessage()), error);
                    SpringApplication.exit(context);
                    System.exit(1);
                })
                .subscribe(currentState -> {
                    LOG.info("Successfully loaded the current state");
                    LOG.info("Clients:");
                    for (var client:currentState.getClients().values()) {
                        LOG.info(String.format("\t%s (%s)", client.getName(), client.getId()));
                    }
                    LOG.info("Device:");
                    for (var device:currentState.getDevices().values()) {
                        LOG.info(String.format("\t%s (%s)", device.getName(), device.getId()));
                    }
                    System.exit(SpringApplication.exit(context));
                });
    }
}
