package com.withsw.oauth.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record NaverResponseDto(
    String resultCode,
    String message,
    NaverProfileDto response
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record NaverProfileDto(
            String id,
            String email,
            String nickname,
            String profileImage
    ) {
    }
}
