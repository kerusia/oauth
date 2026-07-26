package com.withsw.oauth.member.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth.google")
public record GoogleProperties(
        String clientId,
        String clientSecret,
        String authUri,
        String tokenUri,
        String redirectUri,
        String profileUri,
        String authProviderX509CertUrl,
        String grantType
) {
}