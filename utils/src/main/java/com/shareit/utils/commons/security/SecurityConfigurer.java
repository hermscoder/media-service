package com.shareit.utils.commons.security;

import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;



public abstract class SecurityConfigurer extends WebSecurityConfigurerAdapter {

    private static final String[] SWAGGER_UI_PATHS = {
            "/v2/api-docs",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**"
    };

    private final Environment environment;

    protected SecurityConfigurer(Environment environment) {
        this.environment = environment;
    }

    protected abstract AntMatcher getAntMatcher();

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        enableH2ConsoleForTestEnvironment(httpSecurity);
        disableDefaultSpringSecurityLoginScreen(httpSecurity);

        httpSecurity.cors().and().csrf().disable();
        httpSecurity.authorizeRequests()
                .antMatchers(getAntMatcher().getPublicMatchers()).permitAll()
                .antMatchers(SWAGGER_UI_PATHS).permitAll()
                .antMatchers(HttpMethod.GET, getAntMatcher().getPublicMatchersGet()).permitAll()
                .antMatchers(HttpMethod.POST, getAntMatcher().getPublicMatchersPost()).permitAll()
                .antMatchers(HttpMethod.DELETE, getAntMatcher().getPublicMatchersDelete()).permitAll()
                .antMatchers(HttpMethod.PUT, getAntMatcher().getPublicMatchersPut()).permitAll()
                .anyRequest().authenticated();
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    private void enableH2ConsoleForTestEnvironment(HttpSecurity httpSecurity) throws Exception {
        if(Arrays.asList(environment.getActiveProfiles()).contains("test")) {
            httpSecurity.headers().frameOptions().disable();
        }
    }

    private void disableDefaultSpringSecurityLoginScreen(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.httpBasic().disable();
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        //TODO define what would be the origin url to put in the application.properties file
//        configuration.setAllowedOrigins(Arrays.asList(environment.getProperty("allow.origin.url")));
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}