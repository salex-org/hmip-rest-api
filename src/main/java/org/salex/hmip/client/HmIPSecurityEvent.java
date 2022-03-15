package org.salex.hmip.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class HmIPSecurityEvent {
    @JsonProperty("eventTimestamp")
    private Date timestamp;
    @JsonProperty("label")
    private String name;
    @JsonProperty("eventType")
    private String type;

    public Date getTimestamp() {
        return timestamp;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
