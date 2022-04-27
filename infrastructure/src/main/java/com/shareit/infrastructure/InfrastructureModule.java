package com.shareit.infrastructure;

import com.shareit.infrastructure.integration.CloudinaryConfiguration;
import com.shareit.infrastructure.integration.CloudinaryManager;
import com.shareit.infrastructure.integration.CloudinarySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfrastructureModule {
    @Bean
    public CloudinaryManager getCloudinaryManager(CloudinaryConfiguration cloudinaryConfiguration) {
        return new CloudinaryManager(new CloudinarySettings(cloudinaryConfiguration));
    }

}
