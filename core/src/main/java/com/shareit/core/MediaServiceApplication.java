package com.shareit.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableEurekaClient
@EnableJpaRepositories(basePackages = {"com.shareit.data"})
@EntityScan(basePackages = {"com.shareit.domain"})
@ComponentScan(basePackages = {"com.shareit.data", "com.shareit.presentation", "com.shareit.business", "com.shareit.utils", "com.shareit.infrastructure"})
public class MediaServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MediaServiceApplication.class);
    }
}
