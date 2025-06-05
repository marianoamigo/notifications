package com.api.notifications.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JWTUtil {

    //les carga lo que tengamos en application.propperties
    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.issuer}")
    private String issuer;

    @Value("${security.jwt.ttlMillis}")
    private long ttlMillis;

    private Key signingKey;
    private final Logger log = LoggerFactory.getLogger(JWTUtil.class);

    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String create(String id, String subject) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signingKey, SignatureAlgorithm.HS256);

        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        return builder.compact();
    }

    public String getValue(String jwt) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        return claims.getSubject();
    }

    public String getKey(String jwt) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        return claims.getId();
    }

    public String acortaToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } //borra el texto "Bearer " para que no de error el token
        return token;
    }

    public String validaToken(String token) {
        try {
            String idUsuario = getKey(token);
            if (idUsuario.isEmpty()) {
                return null;
            }
            return idUsuario;
        } catch (ExpiredJwtException e) {
            log.warn("Token expirado: " + e.getMessage());
            return  null;
        } catch (JwtException e) {
            log.warn("Token inválido: " + e.getMessage());
            return null;
        }
    }
}

