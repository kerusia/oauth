package com.withsw.oauth.common.auth;

import com.withsw.oauth.member.domain.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class JwtExampleTest {
    private JwtProperties jwtProperties;
    private SecretKey secretKey;

    @BeforeEach
    public void init() {
        jwtProperties = new JwtProperties(
        "bXluYW1laXNrZXJ1c2lhbXluYW1laXNrZXJ1c2lhbXluYW1laXNrZXJ1c2lhbXluYW1laXNrZXJ1c2lhMTIzNA==",
        1800000,
        1209600000,
        "kerusia"
        );
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.secret()));
    }

    @Test
    public void testJwt() {
        String email = "kerusia85@naver.com";
        String role = Role.ADMIN.name();

        String token =
                Jwts.builder()
                    .subject(email)
                    .claim("role", role)
                    .expiration(new Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration()))
                    .issuer(jwtProperties.issuer())
                    .signWith(secretKey, Jwts.SIG.HS512)
                    .compact();

        assertNotNull(token);
        assertEquals(3, token.split("\\.").length);

        Claims claims =
                Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

        assertEquals(email, claims.getSubject());
        assertEquals(role, claims.get("role"));
        assertEquals(jwtProperties.issuer(), claims.getIssuer());
        assertTrue(claims.getExpiration().after(new Date()));
    }
}
