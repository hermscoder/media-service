package com.shareit.infrastructure.integration;

import java.util.HashMap;
import java.util.Map;

public class CloudinarySettings {
    private String cloudName;
    private String apiKey;
    private String apiSecret;
    private Long maxFileSize;

    public CloudinarySettings(CloudinaryConfiguration cloudinaryConfiguration) {
        this.cloudName = cloudinaryConfiguration.getCloudName();
        this.apiKey = cloudinaryConfiguration.getApiKey();
        this.apiSecret = cloudinaryConfiguration.getApiSecret();
        this.maxFileSize = cloudinaryConfiguration.getMaxFileSize();
    }

    public String getCloudName() {
        return cloudName;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public Long getMaxFileSize() {
        return maxFileSize;
    }

    public Map<String, String> getConfigMap(){
        Map<String, String> configmap = new HashMap<>();
        configmap.put("cloud_name", getCloudName());
        configmap.put("api_key", getApiKey());
        configmap.put("api_secret", getApiSecret());
        return configmap;
    }
}
