package com.withsw.oauth.oauth.service;

import com.withsw.oauth.member.domain.Member;
import com.withsw.oauth.member.domain.SocialType;
import com.withsw.oauth.member.dto.KakaoResponseDto;
import com.withsw.oauth.member.service.MemberService;
import com.withsw.oauth.oauth.dto.GoogleResponse;
import com.withsw.oauth.oauth.dto.KakaoResponse;
import com.withsw.oauth.oauth.dto.NaverResponse;
import com.withsw.oauth.oauth.dto.OAuth2Response;
import com.withsw.oauth.oauth.principal.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberService memberService;

    private final RestClient restClient = RestClient.create();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Assert.notNull(userRequest, "userRequest cannot be null");

        // 사용자 정보 조회
        OAuth2Response response = getUserInfo(userRequest);
        if(response == null) {
            throw new NullPointerException("OAuth2User is null");
        }

        // 회원조회
        Member member = memberService.getMemberBySocialId(response.getProviderId());

        // 미가입 상태이면 회원가입
        if(member == null) {
            member = memberService.createOauth(response.getProviderId(), response.getEmail(), response.getNickname(), SocialType.valueOf(response.getProviderName()));
        }

        //return new CustomOAuth2User(oauth2User.getAuthorities(), attributes, oauth2User.getName(), member);
        return new CustomOAuth2User(member, null, null);

    }

    private OAuth2Response getUserInfo(OAuth2UserRequest userRequest) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        RegistrationType registrationType = RegistrationType.valueOf(registrationId);

        switch(registrationType) {
            case google -> {

            }
            case kakao -> {

            }
            case naver -> {

            }
        }

        String accessToken = userRequest.getAccessToken().getTokenValue();
        String accessTokenType = userRequest.getAccessToken().getTokenType().getValue();
        String userInfoUri = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();

        ResponseEntity<String> response = restClient.post()
                .uri(userInfoUri)
                .header(HttpHeaders.AUTHORIZATION, accessTokenType + " " + accessToken)
                .retrieve()
                .toEntity(String.class);
        return null;
    }
}
