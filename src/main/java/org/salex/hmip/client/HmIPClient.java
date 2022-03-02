package org.salex.hmip.client;

import java.util.Map;

public class HmIPClient {
    private String apiVersion;
    private String language;

    private String osType;
    private String osVersion;

    private String accessPointSGTIN;
    private String pin;

    private String deviceManufacturer;
    private String deviceType;
    private String deviceId;

    private String clientId;
    private String clientName;
    private String clientVersion;
    private String clientAuthToken;

    private String authToken;

    private final JsonBodyBuilder jsonBodyBuilder = new JsonBodyBuilder();

    public JsonBodyBuilder jsonBodyBuilder() {
        return jsonBodyBuilder;
    }

    public class JsonBodyBuilder {
        private JsonBodyBuilder() {}
        public Map<String, Object> getHosts() {
            return Map.of(
                    "clientCharacteristics", clientCharacteristics(),
                    "id", accessPointSGTIN
            );
        }

        public Map<String, Object> connectionRequest() {
            return Map.of(
                    "deviceId", deviceId,
                    "deviceName", clientName,
                    "sgtin", accessPointSGTIN
            );
        }

        public Map<String, Object> isRequestAcknowledged() {
            return Map.of(
                    "deviceId", deviceId
            );
        }

        public Map<String, Object> requestAuthToken() {
            return Map.of(
                    "deviceId", deviceId
            );
        }

        public Map<String, Object> confirmAuthToken() {
            return Map.of(
                    "deviceId", deviceId,
                    "authToken", authToken
            );
        }

        public Map<String, Object> getCurrentState() {
            return Map.of("clientCharacteristics", clientCharacteristics());
        }

        private Map<String, Object> clientCharacteristics() {
            return Map.of(
                    "apiVersion", apiVersion,
                    "applicationIdentifier", clientName,
                    "applicationVersion", clientVersion,
                    "deviceManufacturer", deviceManufacturer,
                    "deviceType", deviceType,
                    "languange", language,
                    "osType", osType,
                    "osVersion", osVersion
            );
        }
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getAccessPointSGTIN() {
        return accessPointSGTIN;
    }

    public void setAccessPointSGTIN(String accessPointSGTIN) {
        this.accessPointSGTIN = accessPointSGTIN;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getDeviceManufacturer() {
        return deviceManufacturer;
    }

    public void setDeviceManufacturer(String deviceManufacturer) {
        this.deviceManufacturer = deviceManufacturer;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
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

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
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
}
