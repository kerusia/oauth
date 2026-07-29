package com.withsw.oauth.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KakaoProfileDto(
        String nickname,
        @JsonProperty("profile_image_url") String profileImageUrl
) {
}
