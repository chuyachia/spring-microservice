package com.chuya.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.persistence.criteria.CriteriaBuilder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JWTUtils {
    private final String secret;
    private final Integer validityMillis;
    private final SecretKey secretKey;
    private final JwtParser parser;

    public JWTUtils(@Value("${jwt.secret}") String secret, @Value("${jwt.validity-millis}") Integer validityMillis) {
        this.secret = secret;
        this.validityMillis = validityMillis;
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this. parser = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build();
    }

    public <T> T getClaim(String jwtString, String claim, Class<T> tClass ) {
        return parser.parseClaimsJws(jwtString)
                .getBody()
                .get(claim, tClass);
    }

    public String getSubject(String jwtString) throws JwtException{
        return parser.parseClaimsJws(jwtString)
                .getBody()
                .getSubject();
    }

    public Date getExpiration(String jwtString) throws JwtException{
        return parser.parseClaimsJws(jwtString)
                .getBody()
                .getExpiration();
    }

    public String generateToken(Map<String, Object> claims, String subject) {
        String jwt = Jwts.builder()
                .setSubject(subject)
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + validityMillis))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        return jwt;
    }
}
