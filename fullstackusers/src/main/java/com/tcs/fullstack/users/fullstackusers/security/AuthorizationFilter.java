package com.tcs.fullstack.users.fullstackusers.security;

import java.io.IOException;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.tcs.jwtauthorities.JwtClaimsParser;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    private Environment environment;

    /**
     * 
     * @param authenticationManager
     * @param environment
     */
    public AuthorizationFilter(AuthenticationManager authenticationManager, Environment environment) {
        super(authenticationManager);
        this.environment = environment;
    }

    
    @Override
    protected void doFilterInternal(HttpServletRequest req,
            HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        String authorizationHeader = req.getHeader(environment.getProperty("authorization.token.header.name"));

        if (authorizationHeader == null
                || !authorizationHeader.startsWith(environment.getProperty("authorization.token.header.prefix"))) {
            chain.doFilter(req, res);
            throw new InsufficientAuthenticationException("Authorization header is missing");
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    /**
     * 
     * @param req
     * @return
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) {
        String authorizationHeader = req.getHeader(environment.getProperty("authorization.token.header.name"));

        if (authorizationHeader == null) {
            throw new InsufficientAuthenticationException("Authorization header is missing");
        }

        String token = authorizationHeader.replace(environment.getProperty("authorization.token.header.prefix"), "");
        String tokenSecret = environment.getProperty("token.secret");

        if (tokenSecret == null) {
            throw new InternalAuthenticationServiceException("Token secret is not configured on the server");
        }

        try {

            JwtClaimsParser jwtClaimsParser = new JwtClaimsParser(token, tokenSecret);
            String userId = jwtClaimsParser.getJwtSubject();

            if (userId == null) {
                throw new BadCredentialsException("Invalid token: User ID not found in claims");
            }

            return new UsernamePasswordAuthenticationToken(userId, null, jwtClaimsParser.getUserAuthorities());
        } catch (Exception e) {
            throw new BadCredentialsException("Token validation failed: " + e.getMessage());
        }
    }
}
