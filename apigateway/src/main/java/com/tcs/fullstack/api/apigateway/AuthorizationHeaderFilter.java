package com.tcs.fullstack.api.apigateway;

import java.util.Base64;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

/**
 * This class will create a filter for our routes that checks if the request has
 * a valid header and value pair as well as a valid jwt access token.
 */
@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    @Autowired
    private Environment env;

    public AuthorizationHeaderFilter() {
        super(Config.class);
    }

    public static class Config {

    }

    /**
     * A overrided method that will actually make and apply the filter to our
     * routes. The filter will check if the 'Authorization' Header and 'Bearer
     * JWT_Value' Value are present and valid.
     * 
     * @param config The configuartions to hold the parameters of the filter. We
     *               need to pass a config parameter to run this method even if we
     *               have no configuartions so it can be blank and unused.
     * 
     * @return A Gateway Filter to apply to our routes.
     */
    @Override
    public GatewayFilter apply(Config config) {

        // exchange reads the http object
        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsHeader(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer ", "");

            if (!isJwtValid(jwt)) {
                return onError(exchange, "JWT token is not Valid", HttpStatus.UNAUTHORIZED);
            }

            return chain.filter(exchange); // Chain is the gateway filter chain
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        return response.setComplete();
    }

    /**
     * A helper method to check if the jwt access token is valid with the client
     * attempting to use the application. We check against the tokenSecret value we
     * have in application.properties.
     * 
     * @param jwt The JSON Web Token
     * @return True if valid, False if Invalid
     */
    private boolean isJwtValid(String jwt) {
        try {
            String tokenSecret = env.getProperty("token.secret");
            byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
            SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);

            String subject = null;
            try {
                subject = Jwts.parser()
                        .verifyWith(secretKey)
                        .build()
                        .parseSignedClaims(jwt)
                        .getPayload()
                        .getSubject();
            } catch (Exception e) {
                return false;
            }

            return subject != null && !subject.isEmpty();
        } catch (Exception ex) {
            return false;
        }
    }
}
