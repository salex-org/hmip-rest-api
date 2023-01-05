# Java Client for the Homematic IP Cloud

A Java wrapper for the RESP API of the Homematic IP Cloud.
Since there is no offical documentation i used the code of the [Python wrapper](https://github.com/coreGreenberet/homematicip-rest-api)
to get an idea of how the API works. Thanks to [coreGreenberet](https://github.com/coreGreenberet) for doing the great job of
reverse engineering. **Use this library at your own risk!**

# Dependency
To use the library, add the following dependency to your `pom.xml`:
```xml 
<dependency>
    <groupId>org.salex.hmip</groupId>
    <artifactId>client</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Using SNAPSHOT-Versions (not recommended)
The SNAPSHOT-Versions from CI builds will be published in GitHub Packages.
To use a SNAPSHOT-Version you first you have to add the repository to your `pom.xml`:
```xml
<repositories>
    <repository>
        <id>github</id>
        <name>GitHub Packages</name>
        <url>https://maven.pkg.github.com/salex-org/hmip-rest-api</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
```

After that, you can add a dependency to a SNAPSHOT-Version:
```xml 
<dependency>
    <groupId>org.salex.hmip</groupId>
    <artifactId>client</artifactId>
    <version>1.1.0-SNAPSHOT</version>
</dependency>
```

**It is not recommended to use SNAPSHOT-Versions as they are 'work in progress' with less stability!**

# Loading the configuration and getting the current state
To load the configuration for the client from an `application.yml` you can use the class
`org.salex.hmip.client.HmIPProperties`. Include it into the configuration properties scan
and provide the following properties in your `application.yml`:

```yml
org:
  salex:
    hmip:
      client:
        access-point-sgtin: '{cipher}<some-encrypted-value>'
        pin: '{cipher}<some-encrypted-value>'
        device-id: '{cipher}<some-encrypted-value>'
        client-id: '{cipher}<some-encrypted-value>'
        client-name: '<some-value>'
        client-auth-token: '{cipher}<some-encrypted-value>'
        auth-token: '{cipher}<some-encrypted-value>'
```
**You should not store any tokens or other secrets as plain text in the configuration!**

You can use the [property encryption feature of Spring Cloud Config](https://cloud.spring.io/spring-cloud-static/spring-cloud-config/1.3.0.RELEASE/#_encryption_and_decryption) to decrypt the
secret configuration values, and the [Spring Cloud CLI for manual encryption](https://cloud.spring.io/spring-cloud-cli/reference/html/#_encryption_and_decryption)
of the values. You can provide the encryption key by setting the environment variable `ENCRYPT_KEY`.

With the loaded (and decrypted) properties you can easily create and use the client:

```java
final var properties = ... // Should be injected
HmIPConfiguration.builder()
    .properties(properties)
    .build()
    .map(HmIPClient::new)
    .flatMap(client -> client.loadCurrentState())
    .doOnError(error -> {
        LOG.error(String.format("Failed to load the current state: %s", error.getMessage()));
        ...
    })
    .subscribe(currentState -> {
        LOG.info("Successfully loaded the current state");
        ...
        // Do some world-improving things :-)
    });
```

As an alternative you can create the client in a bean method so that you can use it by injection:

```java
@Bean
HmIPClient createHomematicClient(HmIPProperties properties) {
    return HmIPConfiguration.builder()
            .properties(properties)
            .build()
            .map(HmIPClient::new)
            .block();
}
```

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
    }).subscribe(config -> {
        LOG.info("Successfully registered new client");
        LOG.info(String.format("Device ID: %s", config.getDeviceId()));
        LOG.info(String.format("Client ID: %s", config.getClientId()));
        LOG.info(String.format("Client Auth Token: %s", config.getClientAuthToken()));
        LOG.info(String.format("Auth Token: %s", config.getAuthToken()));
        ...
        // Encrypt these values and put them to the configuration
        // of your client app for later usage
    });
```
All you need is the SGTIN (Serialized Global Trade Item Number) of your access point, the PIN (if you have assigned one)
and a name for the client. You also have to acknowledge the client registration by pressing the "blue button" on your
access point, when you are prompted in the log.

# Examples
Please find some examples in the [Examples-Project](https://github.com/salex-org/hmip-rest-api-examples)

# Work in Progress
The development of the library is 'work in progress', so actually only a few features are available. 
