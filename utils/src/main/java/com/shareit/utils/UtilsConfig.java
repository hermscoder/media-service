package com.shareit.utils;

import com.shareit.utils.commons.provider.DateProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilsConfig {

    @Bean
    public DateProvider getDateProvider() {
        return new DateProvider();
    }
}
