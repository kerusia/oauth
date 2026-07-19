package com.withsw.oauth.common.auth;

import org.springframework.http.HttpHeaders;

public final class JwtConstants {
    private JwtConstants() {}

    public static final String AUTHORIZATION_HEADER = HttpHeaders.AUTHORIZATION;
    public static final String TOKEN_TYPE = "Bearer";
    public static final String TOKEN_PREFIX = TOKEN_TYPE + " ";

    public static final String JWT_ID = "jti";
    public static final String ROLE = "role";
    public static final String SUBJECT = "sub";
    public static final String ISSUER = "iss";
    public static final String AUDIENCE = "aud";
    public static final String EXPIRATION_TIME = "exp";
    public static final String NOT_BEFORE = "nbf";
    public static final String ISSUED_AT = "iat";
}
