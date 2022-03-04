package org.salex.hmip.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * Properties for the configuration of the client library.
 * Can be used when building a client configuration (see {@link HmIPConfiguration.Builder#properties(HmIPProperties)}).
 */
@ConfigurationProperties(prefix = "org.salex.hmip.client")
@ConstructorBinding
public class HmIPProperties {
    private final String accessPointSGTIN;
    private final String pin;
    private final String deviceId;
    private final String clientId;
    private final String clientName;
    private final String clientAuthToken;
    private final String authToken;

    public HmIPProperties(String accessPointSGTIN, String pin, String deviceId, String clientId, String clientName, String clientAuthToken, String authToken) {
        this.accessPointSGTIN = accessPointSGTIN;
        this.pin = pin;
        this.deviceId = deviceId;
        this.clientId = clientId;
        this.clientName = clientName;
        this.clientAuthToken = clientAuthToken;
        this.authToken = authToken;
    }

    public String getAccessPointSGTIN() {
        return accessPointSGTIN;
    }

    public String getPin() {
        return pin;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientAuthToken() {
        return clientAuthToken;
    }

    public String getAuthToken() {
        return authToken;
    }
}
