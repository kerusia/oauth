package com.withsw.oauth.oauth.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.withsw.oauth.member.domain.SocialType;
import com.withsw.oauth.member.dto.GoogleResponseDto;
import com.withsw.oauth.oauth.service.RegistrationType;

import java.util.Map;

public class GoogleResponse implements OAuth2Response {
    private final GoogleResponseDto googleResponseDto;

    public GoogleResponse(Map<String, Object> oauth2User) {
        ObjectMapper objectMapper = new ObjectMapper();
        this.googleResponseDto = objectMapper.convertValue(oauth2User, GoogleResponseDto.class);
    }

    @Override
    public String getProviderId() {
        return googleResponseDto.getSub();
    }

    @Override
    public String getProviderName() {
        return SocialType.GOOGLE.name();
    }

    @Override
    public String getEmail() {
        return googleResponseDto.getEmail();
    }

    @Override
    public String getNickname() {
        return googleResponseDto.getName();
    }
}
