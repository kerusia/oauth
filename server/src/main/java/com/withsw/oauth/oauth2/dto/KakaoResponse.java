package com.withsw.oauth.oauth2.dto;

import com.withsw.oauth.member.domain.SocialType;
import com.withsw.oauth.member.dto.KakaoResponseDto;

public class KakaoResponse implements OAuth2Response {
    private final KakaoResponseDto kakaoResponseDto;

    public KakaoResponse(KakaoResponseDto kakaoResponseDto) {
        this.kakaoResponseDto = kakaoResponseDto;
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
