package com.withsw.oauth.member.service;

import com.withsw.oauth.member.dto.AccessTokenDto;
import com.withsw.oauth.member.dto.KakaoResponseDto;
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
public class KakaoService {

    private final KakaoProperties kakaoProperties;

    public AccessTokenDto getAccessToken(String code) {
        RestClient restClient = RestClient.create();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("code", code);
        params.add("client_id", kakaoProperties.clientId());
        params.add("client_secret", kakaoProperties.clientSecret());
        params.add("redirect_uri", kakaoProperties.redirectUri());
        params.add("grant_type", kakaoProperties.grantType());

        ResponseEntity<AccessTokenDto> response = restClient.post()
                .uri(kakaoProperties.tokenUri())
                .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(params)
                .retrieve() // body 값만 추출
                .toEntity(AccessTokenDto.class);

        //System.out.println("응답 Access Token JSON: " + response.getBody());

        return response.getBody();
    }

    public KakaoResponseDto getProfile(String accessToken, String tokenType) {

        RestClient restClient = RestClient.create();
        ResponseEntity<KakaoResponseDto> response = restClient.post()
                .uri(kakaoProperties.profileUri())
                .header(HttpHeaders.AUTHORIZATION, tokenType + " " + accessToken)
                .retrieve()
                .toEntity(KakaoResponseDto.class);

        //System.out.println("응답 Profile JSON: " + response.getBody());

        return response.getBody();
    }
}
