package com.tcs.jwtauthorities;

import java.util.Base64;
import java.util.Collection;

import javax.crypto.SecretKey;

import org.springframework.security.core.GrantedAuthority;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class JwtClaimsParser {

    Jwt<?, ?> jwtObject;

    public JwtClaimsParser(String jwt, String secretToken) {
        this.jwtObject = parseJwt(jwt, secretToken);
    }

    Jwt<?, ?> parseJwt(String jwtString, String secretToken) {
        byte[] secretKeyBytes = Base64.getEncoder().encode(secretToken.getBytes());
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);

        JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();

        return jwtParser.parse(jwtString);
    }

    public Collection<? extends GrantedAuthority> getUserAuthorities() {
        List<String> scopes = ((Claims) jwtObject.getPayload()).get("scope", List.class);

        return scopes.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

    }

    public String getJwtSubject() {
        return ((Claims)jwtObject.getPayload()).getSubject();
    }
}
