package com.withsw.oauth.common.auth;

public final class JwtTokenExtractor {

    private JwtTokenExtractor() {

    }

    public static String extract(String authorizationHeader) {
        if(authorizationHeader == null) {
            throw new IllegalArgumentException("Authorization header cannot be null");
        }

        if(!authorizationHeader.startsWith(JwtConstants.TOKEN_PREFIX)) {
            throw new IllegalArgumentException("Authorization header does not start with " + JwtConstants.TOKEN_PREFIX);
        }

        return authorizationHeader.substring(JwtConstants.TOKEN_PREFIX.length());
    }
}
