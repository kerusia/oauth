package com.withsw.oauth.oauth.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.withsw.oauth.member.domain.SocialType;
import com.withsw.oauth.member.dto.NaverResponseDto;
import com.withsw.oauth.oauth.service.RegistrationType;

import java.util.Map;

public class NaverResponse implements OAuth2Response {
    private final NaverResponseDto naverResponseDto;

    public NaverResponse(Map<String, Object> oauth2User) {
        ObjectMapper objectMapper = new ObjectMapper();
        this.naverResponseDto = objectMapper.convertValue(oauth2User, NaverResponseDto.class);
    }

    @Override
    public String getProviderId() {
        return naverResponseDto.response().id();
    }

    @Override
    public String getProviderName() {
        return SocialType.NAVER.name();
    }

    @Override
    public String getEmail() {
        return naverResponseDto.response().email();
    }

    @Override
    public String getNickname() {
        return naverResponseDto.response().nickname();
    }
}
