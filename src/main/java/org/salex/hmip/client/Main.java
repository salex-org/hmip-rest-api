package org.salex.hmip.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    @Autowired
    private ConfigurableApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("Registering new client");
        final var clientName = "Java Test";
        final var accessPointSGTIN = "3014-F711-A000-03DD-89AD-F7FE";
        HmIPCloud.registerClient(accessPointSGTIN, clientName)
                .doOnError(error -> {
                    LOG.error("Failed to register new client");
                    SpringApplication.exit(context);
                    System.exit(1);
                }).subscribe(client -> {
                    LOG.info("Successfully registered new client");
                    LOG.info(String.format("Device ID: %s", client.getDeviceId()));
                    LOG.info(String.format("Client ID: %s", client.getClientId()));
                    LOG.info(String.format("Client Auth Token: %s", client.getClientAuthToken()));
                    LOG.info(String.format("Auth Token: %s", client.getAuthToken()));
                    LOG.info("Finished and saying goodbye");
                    System.exit(SpringApplication.exit(context));
                });
    }
}
