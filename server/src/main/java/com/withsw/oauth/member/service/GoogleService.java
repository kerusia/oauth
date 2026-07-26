package com.withsw.oauth.member.service;

import com.withsw.oauth.member.dto.AccessTokenDto;
import com.withsw.oauth.member.dto.GoogleProfileDto;
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
public class GoogleService {

    private final GoogleProperties googleProperties;

    public AccessTokenDto getAccessToken(String code) {
        // 인가코드, client_id, client_secret, redirect_uri, grant_type

        // Spring6 부터 RestTemplate Deprecated되고 RestClient 사용 권장
        RestClient restClient = RestClient.create();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("code", code);
        params.add("client_id", googleProperties.clientId());
        params.add("client_secret", googleProperties.clientSecret());
        params.add("redirect_uri", googleProperties.redirectUri());
        params.add("grant_type", googleProperties.grantType());

        ResponseEntity<AccessTokenDto> response = restClient.post()
                .uri(googleProperties.tokenUri())
                .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(params)
                .retrieve() // body 값만 추출
                .toEntity(AccessTokenDto.class);

        //System.out.println("응답 Access Token JSON: " + response.getBody());

        return response.getBody();
    }

    public GoogleProfileDto getProfile(String accessToken, String tokenType) {

        RestClient restClient = RestClient.create();
        ResponseEntity<GoogleProfileDto> response = restClient.post()
                .uri(googleProperties.profileUri())
                .header(HttpHeaders.AUTHORIZATION, tokenType + " " + accessToken)
                .retrieve()
                .toEntity(GoogleProfileDto.class);

        //System.out.println("응답 Profile JSON: " + response.getBody());

        return response.getBody();
    }
}
