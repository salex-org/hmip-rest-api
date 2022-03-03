package org.salex.hmip.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Map;

public class HmIPState {
    public static class Device {
        @JsonProperty("id")
        private String id;
        @JsonProperty("label")
        private String name;
        @JsonProperty("type")
        private String type;
        @JsonProperty("modelType")
        private String model;
        @JsonProperty("serializedGlobalTradeItemNumber")
        private String sgtin;
        @JsonProperty("functionalChannels")
        private Map<String, Object> channels;
        @JsonProperty("lastStatusUpdate")
        private Date statusTimestamp;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public String getModel() {
            return model;
        }

        public String getSGTIN() {
            return sgtin;
        }

        public Map<String, Object> getChannels() {
            return channels;
        }

        public Date getStatusTimestamp() {
            return statusTimestamp;
        }
    }
    public static class Client {
        @JsonProperty("id")
        private String id;
        @JsonProperty("label")
        private String name;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
    @JsonProperty("devices")
    private Map<String, Device> devices;
    @JsonProperty("clients")
    private Map<String, Client> clients;

    public Map<String, Device> getDevices() {
        return devices;
    }

    public Map<String, Client> getClients() {
        return clients;
    }
}
