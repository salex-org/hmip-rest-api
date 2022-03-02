package org.salex.hmip.client;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HmIPHosts {
    @JsonProperty("urlREST")
    private String restURL;

    @JsonProperty("urlWebSocket")
    private String webSocketURL;

    @JsonProperty("apiVersion")
    private String apiVersion;

    @JsonProperty("primaryAccessPointId")
    private String primaryAccessPointId;

    @JsonProperty("requestingAccessPointId")
    private String requestingAccessPointId;

    public String getRestURL() {
        return restURL;
    }

    public String getWebSocketURL() {
        return webSocketURL;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public String getPrimaryAccessPointId() {
        return primaryAccessPointId;
    }

    public String getRequestingAccessPointId() {
        return requestingAccessPointId;
    }
}
