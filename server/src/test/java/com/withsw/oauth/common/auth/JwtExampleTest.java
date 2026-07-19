package com.withsw.oauth.common.auth;

import com.withsw.oauth.member.domain.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
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

    @Test
    public void testParseJwt() {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJrZXJ1c2lhODVAbmF2ZXIuY29tIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3ODQ0NDY0MDMsImV4cCI6MTc4NDQ0ODIwM30.omWM9_yMEK1MtAoy8hi0uQw8mQ_O0chas3Gh4tQ7bQoJsvL3i2o3xHoCNeV4sU08lYwmp7JEPpGxu9j4KF_E5g";
        String[] parts = token.split("\\.");

        assertEquals(3, parts.length);

        System.out.println(parts[0]);
        System.out.println(".");
        System.out.println(parts[1]);
        System.out.println(".");
        System.out.println(parts[2]);

        String header = new String(Decoders.BASE64.decode(parts[0]), StandardCharsets.UTF_8);
        String payload = new String(Decoders.BASE64.decode(parts[1]), StandardCharsets.UTF_8);

        System.out.println(header);
        System.out.println(payload);
    }

    @Test
    public void testVerifyJwt() {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJrZXJ1c2lhODVAbmF2ZXIuY29tIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3ODQ0NDY0MDMsImV4cCI6MTc4NDQ0ODIwM30.omWM9_yMEK1MtAoy8hi0uQw8mQ_O0chas3Gh4tQ7bQoJsvL3i2o3xHoCNeV4sU08lYwmp7JEPpGxu9j4KF_E5g";
        String[] parts = token.split("\\.");

        assertEquals(3, parts.length);

        boolean isSigned =
                Jwts.parser()
                        .verifyWith(secretKey)
                        .build()
                        .isSigned(token);

        assertTrue(isSigned);

        Claims claims =
                Jwts.parser()
                        .verifyWith(secretKey)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

        System.out.println(claims.toString());
    }
}
