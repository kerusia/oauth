package com.withsw.oauth.member.service;

import com.withsw.oauth.member.dto.AccessTokenDto;
import com.withsw.oauth.member.dto.KakaoResponseDto;
import com.withsw.oauth.member.dto.NaverResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Service
public class NaverService {

    private final NaverProperties naverProperties;

    public AccessTokenDto getAccessToken(String code) {
        RestClient restClient = RestClient.create();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("code", code);
        params.add("client_id", naverProperties.clientId());
        params.add("client_secret", naverProperties.clientSecret());
        params.add("redirect_uri", naverProperties.redirectUri());
        params.add("grant_type", naverProperties.grantType());

        ResponseEntity<AccessTokenDto> response = restClient.post()
                .uri(naverProperties.tokenUri())
                .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(params)
                .retrieve() // body 값만 추출
                .toEntity(AccessTokenDto.class);

        //System.out.println("응답 Access Token JSON: " + response.getBody());

        return response.getBody();
    }

    public NaverResponseDto getProfile(String accessToken, String tokenType) {

        RestClient restClient = RestClient.create();
        ResponseEntity<NaverResponseDto> response = restClient.post()
                .uri(naverProperties.profileUri())
                .header(HttpHeaders.AUTHORIZATION, tokenType + " " + accessToken)
                .retrieve()
                .toEntity(NaverResponseDto.class);

        //System.out.println("응답 Profile JSON: " + response.getBody());

        return response.getBody();
    }
}
