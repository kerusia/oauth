package com.withsw.oauth.member.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth.kakao")
public record KakaoProperties(
        String clientId,
        String clientSecret,
        String authUri,
        String tokenUri,
        String redirectUri,
        String profileUri,
        String grantType
) {
}