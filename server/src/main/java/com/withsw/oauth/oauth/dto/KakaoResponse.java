package com.withsw.oauth.oauth.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.withsw.oauth.member.domain.SocialType;
import com.withsw.oauth.member.dto.KakaoResponseDto;
import com.withsw.oauth.oauth.service.RegistrationType;

import java.util.Map;

public class KakaoResponse implements OAuth2Response {
    private final KakaoResponseDto kakaoResponseDto;

    public KakaoResponse(Map<String, Object> oauth2User) {
        ObjectMapper objectMapper = new ObjectMapper();
        this.kakaoResponseDto = objectMapper.convertValue(oauth2User, KakaoResponseDto.class);
    }

    @Override
    public String getProviderId() {
        return kakaoResponseDto.id();
    }

    @Override
    public String getProviderName() {
        return SocialType.KAKAO.name();
    }

    @Override
    public String getEmail() {
        return kakaoResponseDto.kakaoAccount().email();
    }

    @Override
    public String getNickname() {
        return kakaoResponseDto.kakaoAccount().profile().nickname();
    }
}
