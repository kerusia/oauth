package com.withsw.oauth.common.util;

import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

public class KeyGeneratorTest {

    @Test
    public void generateJwtSecretKey() {
        String jwtSecretKey = "mynameiskerusiamynameiskerusiamynameiskerusiamynameiskerusia1234";
        String encodedSecretKey = Encoders.BASE64.encode(jwtSecretKey.getBytes(StandardCharsets.UTF_8));

        System.out.println("Encoded secret key: " + encodedSecretKey);
    }
}
