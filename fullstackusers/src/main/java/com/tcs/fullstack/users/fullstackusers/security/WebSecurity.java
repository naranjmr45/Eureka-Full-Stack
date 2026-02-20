package com.tcs.fullstack.users.fullstackusers.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

@Configuration // This annotaion tells spring to create an instance of this class for us
@EnableWebSecurity
public class WebSecurity {

    private Environment environment;

    public WebSecurity(Environment environment) { // Configs from application.properties are loaded into the enviroment variable where we can then access them
        this.environment = environment;
    }

    @Bean // This annotation puts this method in spring annotation context allowing spring to use it whenever it needs to
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        
        http.csrf((csrf)->csrf.disable()); // Disables Cross Side Request Forgery Protections

        http.authorizeHttpRequests((authz) -> authz
            .requestMatchers("/users").access(new WebExpressionAuthorizationManager("hasIpAddress('"+environment.getProperty("gateway.ip")+"')")) // Allows any user to send a request to this endpoint
            .requestMatchers("/h2-console/**").permitAll()) // Allows us to access the sqlite database
            .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Tells spring we are stateless
        
        http.headers((headers)->headers.frameOptions((frameOptions)->frameOptions.sameOrigin())); // This is mainly for h2-console

        return http.build();
    }

}
