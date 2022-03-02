package org.salex.hmip.client;

import java.util.Locale;

public class HmIPClientCharacteristics {
    private String apiVersion;
    private String applicationIdentifier;
    private String applicationVersion;
    private String deviceManufacturer;
    private String deviceType;
    private String language;
    private String osType;
    private String osVersion;

    public static class Builder {
        private final HmIPClientCharacteristics characteristics;
        private Builder() {
            characteristics = new HmIPClientCharacteristics();
            characteristics.setApiVersion("12");
            characteristics.setApplicationIdentifier("Java Client");
            characteristics.setApplicationVersion("1.0.0");
            characteristics.setDeviceManufacturer("none");
            characteristics.setDeviceType("Computer");
            characteristics.setLanguage(Locale.getDefault().toLanguageTag());
            characteristics.setOsType(System.getProperty("os.name"));
            characteristics.setOsVersion(System.getProperty("os.version"));
        }
        public Builder applicationIdentifier(String applicationIdentifier) {
            characteristics.setApplicationIdentifier(applicationIdentifier);
            return this;
        }
        public HmIPClientCharacteristics build() {
            return characteristics;
        }
    }

    public static HmIPClientCharacteristics.Builder builder() {
        return new HmIPClientCharacteristics.Builder();
    }

    private HmIPClientCharacteristics() {}

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getApplicationIdentifier() {
        return applicationIdentifier;
    }

    public void setApplicationIdentifier(String applicationIdentifier) {
        this.applicationIdentifier = applicationIdentifier;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
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
}
