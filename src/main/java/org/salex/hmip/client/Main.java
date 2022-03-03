package org.salex.hmip.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Spring Boot command line app to register a new client.
 */
@SpringBootApplication
public class Main implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    @Autowired
    private ConfigurableApplicationContext context;

    public static void main(String[] args) {
        if(args.length < 2) {
            LOG.error("Missing arguments");
            LOG.info("Usage: <executable> <access-point-sgtin> <client-name>");
            System.exit(1);
        } else {
            SpringApplication.run(Main.class, args);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        final var accessPointSGTIN = args[0];
        final var clientName = args[1];
        HmIPConfiguration.builder()
                .clientName(clientName)
                .accessPointSGTIN(accessPointSGTIN)
                .build()
                .flatMap(config -> config.registerClient())
                .doOnError(error -> {
                    LOG.error(String.format("Failed to register new client: %s", error.getMessage()));
                    SpringApplication.exit(context);
                    System.exit(1);
                }).subscribe(config -> {
                    LOG.info("Successfully registered new client");
                    LOG.info(String.format("Device ID: %s", config.getDeviceId()));
                    LOG.info(String.format("Client ID: %s", config.getClientId()));
                    LOG.info(String.format("Client Auth Token: %s", config.getClientAuthToken()));
                    LOG.info(String.format("Auth Token: %s", config.getAuthToken()));
                    LOG.info("Finished and saying goodbye");
                    System.exit(SpringApplication.exit(context));
                });
    }
}
