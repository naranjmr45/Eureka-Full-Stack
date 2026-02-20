package com.tcs.fullstack.users.fullstackusers.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

import com.tcs.fullstack.users.fullstackusers.service.UsersService;

@Configuration // This annotaion tells spring to create an instance of this class for us
@EnableWebSecurity
public class WebSecurity {

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private UsersService usersService;

    private Environment environment;

    public WebSecurity(Environment environment, BCryptPasswordEncoder bCryptPasswordEncoder, UsersService usersService) { // Configs from application.properties are loaded into the enviroment variable where we can then access them
        this.environment = environment; // Configs from application.properties are loaded into the enviroment variable where we can then access them
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.usersService = usersService;
    }

    @Bean // This annotation puts this method in spring annotation context allowing spring to use it whenever it needs to
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder
                = http.getSharedObject(AuthenticationManagerBuilder.class); // This gets the authenticationManager

        authenticationManagerBuilder.userDetailsService(usersService).passwordEncoder(bCryptPasswordEncoder);

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        AuthenticationFilter authenticationFilter = 
            new AuthenticationFilter(authenticationManager, usersService, environment);
        authenticationFilter.setFilterProcessesUrl(environment.getProperty("login.url.path"));

        http.csrf((csrf) -> csrf.disable()); // Disables Cross Side Request Forgery Protections

        http.authorizeHttpRequests((authz) -> authz
                .requestMatchers("/users/**").access(new WebExpressionAuthorizationManager("hasIpAddress('" + environment.getProperty("gateway.ip") + "')")) // Allows any user to send a request to this endpoint
                .requestMatchers("/h2-console/**").permitAll()) // Allows us to access the sqlite database
                .addFilter(authenticationFilter)
                .authenticationManager(authenticationManager)
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Tells spring we are stateless

        http.headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.sameOrigin())); // This is mainly for h2-console

        return http.build();
    }

}
