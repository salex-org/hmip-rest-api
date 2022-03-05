package org.salex.hmip.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Date;
import java.util.Map;

/**
 * Representing parts of the Current State requested from the Homemativ IP Cloud.
 */
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
        private Map<String, FunctionalChannel> channels;
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

        public Map<String, FunctionalChannel> getChannels() {
            return channels;
        }

        public Date getStatusTimestamp() {
            return statusTimestamp;
        }
    }
    @JsonTypeInfo(use=JsonTypeInfo.Id.NAME, property="functionalChannelType", defaultImpl = FunctionalChannel.class)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = ClimateSensorChannel.class, name = "CLIMATE_SENSOR_CHANNEL")
    })
    public static class FunctionalChannel  {
        @JsonProperty("functionalChannelType")
        private String type;

        public String getType() {
            return type;
        }
    }
    public static class ClimateSensorChannel extends FunctionalChannel {
        @JsonProperty("actualTemperature")
        private double temperature;
        @JsonProperty("humidity")
        private int humidity;
        @JsonProperty("vaporAmount")
        private double vaporAmount;

        public double getTemperature() {
            return temperature;
        }

        public int getHumidity() {
            return humidity;
        }

        public double getVaporAmount() {
            return vaporAmount;
        }
    }
    public static class Client {
        @JsonProperty("id")
        private String id;
        @JsonProperty("label")
        private String name;
        @JsonProperty("createdAtTimestamp")
        private Date createdTimestamp;
        @JsonProperty("lastSeenAtTimestamp")
        private Date lastSeenTimestamp;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Date getCreatedTimestamp() {
            return createdTimestamp;
        }

        public Date getLastSeenTimestamp() {
            return lastSeenTimestamp;
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