package com.tcs.fullstack.api.apigateway.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * This class is used to handle the Cross-Origin Reasourse Sharing issue (CORS).
 * CORS issues will appear if the front end is trying to commonicate with the
 * backend because the backend thinks it's a breach. We have to allow certain
 * sites to access the backend in order to use the application from the client
 * side.
 */
@Configuration
public class GatewayCorsConfig {

    /**
     * This method sets the configurations needed to allow the front end to access
     * the backend.
     * 
     * @return A Filter with the configurations set.
     */
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Remove the trailing slash from the origin URL
        config.setAllowedOrigins(Arrays.asList("https://didactic-fiesta-5gpgwjjwrr4jh7rjg-8080.app.github.dev"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setExposedHeaders(Arrays.asList("token", "userid"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}
