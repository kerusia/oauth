package com.withsw.oauth.member.service;

import com.withsw.oauth.member.dto.AccessTokenDto;
import com.withsw.oauth.member.dto.GoogleProfileDto;
import org.springframework.stereotype.Service;

@Service
public class GoogleService {
    public AccessTokenDto getAccessToken(String code) {
        return null;
    }

    public GoogleProfileDto getProfile(String accessToken) {
        return null;
    }
}
