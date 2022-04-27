package com.shareit.infrastructure.security;

import com.shareit.utils.commons.security.AntMatcher;
import com.shareit.utils.commons.security.SecurityConfigurer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class MediaServiceSecurityConfigurer extends SecurityConfigurer {

    private static  final String [] PUBLIC_MATCHERS = {
            "/healthcheck",
            "/h2/**",
            "/auth/forgot/**",
            "/v1/media/**"
    };

    private static  final String [] PUBLIC_MATCHERS_GET = {
            "/v1/media/**",
            "/property/**"
    };
    private static  final String [] PUBLIC_MATCHERS_POST = {
            "/auth/forgot/**"
    };
    private static  final String [] PUBLIC_MATCHERS_DELETE = {
    };
    private static  final String [] PUBLIC_MATCHERS_PUT = {
    };

    protected MediaServiceSecurityConfigurer(Environment environment) {
        super(environment);
    }

    @Override
    protected AntMatcher getAntMatcher() {
        return AntMatcher.newBuilder()
                .publicMatchers(PUBLIC_MATCHERS)
                .publicMatchersGet(PUBLIC_MATCHERS_GET)
                .publicMatchersPost(PUBLIC_MATCHERS_POST)
                .publicMatchersDelete(PUBLIC_MATCHERS_DELETE)
                .publicMatchersPut(PUBLIC_MATCHERS_PUT).build();
    }

}
