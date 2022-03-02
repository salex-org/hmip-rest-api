package org.salex.hmip.client;

public class HmIPClient {
    private HmIPClientCharacteristics characteristics;
    private String accessPointSGTIN;
    private String deviceId;
    private String clientId;
    private String clientName;
    private String clientAuthToken;
    private String authToken;
    private String pin;

    public HmIPClientCharacteristics getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(HmIPClientCharacteristics characteristics) {
        this.characteristics = characteristics;
    }

    public String getAccessPointSGTIN() {
        return accessPointSGTIN;
    }

    public void setAccessPointSGTIN(String accessPointSGTIN) {
        this.accessPointSGTIN = accessPointSGTIN;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientAuthToken() {
        return clientAuthToken;
    }

    public void setClientAuthToken(String clientAuthToken) {
        this.clientAuthToken = clientAuthToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
