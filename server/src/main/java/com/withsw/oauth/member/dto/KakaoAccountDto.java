package com.withsw.oauth.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KakaoAccountDto(
        @JsonProperty("profile") KakaoProfileDto profile,
        String email,
        @JsonProperty("is_email_verified") String isEmailVerified
) {
}
