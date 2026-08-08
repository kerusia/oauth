package com.withsw.oauth.oauth2.dto;

import com.withsw.oauth.member.domain.SocialType;
import com.withsw.oauth.member.dto.GoogleResponseDto;

public class GoogleResponse implements OAuth2Response {
    private final GoogleResponseDto googleResponseDto;

    public GoogleResponse(GoogleResponseDto googleResponseDto) {
        this.googleResponseDto = googleResponseDto;
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
