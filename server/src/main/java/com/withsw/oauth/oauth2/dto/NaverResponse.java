package com.withsw.oauth.oauth2.dto;

import com.withsw.oauth.member.domain.SocialType;
import com.withsw.oauth.member.dto.NaverResponseDto;

public class NaverResponse implements OAuth2Response {
    private final NaverResponseDto naverResponseDto;

    public NaverResponse(NaverResponseDto naverResponseDto) {
        this.naverResponseDto = naverResponseDto;
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
