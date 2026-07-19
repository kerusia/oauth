package com.withsw.oauth.common.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final JwtProperties jwtProperties;
    private final SecretKey jwtSecretKey;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.jwtSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.secret()));
    }

    public String createToken(String email, String role) {
        Date now = new Date();

        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + jwtProperties.accessTokenExpiration()))
                .signWith(jwtSecretKey, Jwts.SIG.HS512)
                .compact();
    }

    public boolean validateToken(String token) {
        return Jwts.parser()
                .verifyWith(jwtSecretKey)
                .build()
                .isSigned(token);
    }
}
