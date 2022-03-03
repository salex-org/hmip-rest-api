# Java Client for the Homematic IP Cloud

A Java wrapper for the RESP API of the Homematic IP Cloud.
Since there is no offical documentation i used the code of the [Python wrapper](https://github.com/coreGreenberet/homematicip-rest-api)
to get an idea of how the API works. Thanks to [coreGreenberet](https://github.com/coreGreenberet) for doing the great job of
reverse engineering. **Use this library at your own risk!**

# Registering a new client
To register a new client in the Homematic IP Cloud you can use the client library:
```java
final var clientName = ...
final var accessPointSGTIN = ...        
HmIPConfiguration.builder()
    .clientName(clientName)
    .accessPointSGTIN(accessPointSGTIN)
    .build()
    .flatMap(config -> config.registerClient())
    .doOnError(error -> {
        LOG.error(String.format("Failed to register new client: %s", error.getMessage()));
        ...
    }).subscribe(client -> {
        LOG.info("Successfully registered new client");
        LOG.info(String.format("Device ID: %s", client.getDeviceId()));
        LOG.info(String.format("Client ID: %s", client.getClientId()));
        LOG.info(String.format("Client Auth Token: %s", client.getClientAuthToken()));
        LOG.info(String.format("Auth Token: %s", client.getAuthToken()));
        ...
    });
```
All you need is the SGTIN of your access point and a name for the client. You also have to acknowledge the client
registration by pressing the "blue button" on your access point, when you are prompted in the log.

# Work in Progress
The development of the library is 'work in progress', so actually only a few features are available. 