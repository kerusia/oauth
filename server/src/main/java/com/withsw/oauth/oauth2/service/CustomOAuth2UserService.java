package com.withsw.oauth.oauth2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.withsw.oauth.member.domain.Member;
import com.withsw.oauth.member.domain.Role;
import com.withsw.oauth.member.domain.SocialType;
import com.withsw.oauth.member.dto.GoogleResponseDto;
import com.withsw.oauth.member.dto.KakaoResponseDto;
import com.withsw.oauth.member.dto.NaverResponseDto;
import com.withsw.oauth.member.service.MemberService;
import com.withsw.oauth.oauth2.dto.GoogleResponse;
import com.withsw.oauth.oauth2.dto.KakaoResponse;
import com.withsw.oauth.oauth2.dto.NaverResponse;
import com.withsw.oauth.oauth2.dto.OAuth2Response;
import com.withsw.oauth.oauth2.principal.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClient;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberService memberService;
    private final ObjectMapper objectMapper;
    private final RestClient restClient = RestClient.create();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Assert.notNull(userRequest, "userRequest cannot be null");

        try {
            String registrationId = userRequest.getClientRegistration().getRegistrationId();

            // 사용자 정보 조회
            OAuth2Response response = getUserInfo(userRequest);
            if(response == null) {
                throw new NullPointerException("OAuth2User is null");
            }

            // 회원조회
            Member member = memberService.getMemberBySocialTypeAndSocialId(SocialType.valueOf(registrationId.toUpperCase()), response.getProviderId());

            // 미가입 상태이면 회원가입
            if(member == null) {
                member = memberService.createOauth(response.getProviderId(), response.getEmail(), response.getNickname(), SocialType.valueOf(response.getProviderName()));
            }

            // 권한 생성
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + Role.USER.name()));

            return new CustomOAuth2User(member, null, authorities);
        }
        catch(OAuth2UserInfoException e) {
            throw new OAuth2AuthenticationException(new OAuth2Error("oauth2_user_info_error"), "OAuth 사용자 정보 처리에 실패했습니다.", e);
        }
    }

    private OAuth2Response getUserInfo(OAuth2UserRequest userRequest) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        RegistrationType registrationType = RegistrationType.valueOf(registrationId);
        String accessToken = userRequest.getAccessToken().getTokenValue();
        String accessTokenType = userRequest.getAccessToken().getTokenType().getValue();
        String userInfoUri = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();

        String response = restClient.post()
                .uri(userInfoUri)
                .header(HttpHeaders.AUTHORIZATION, accessTokenType + " " + accessToken)
                .retrieve()
                .body(String.class);

        return switch(registrationType) {
            case kakao -> convertKakao(response);
            case naver -> convertNaver(response);
            default -> throw new IllegalArgumentException("Unsupported OAuth Provider: " + registrationId);
        };
    }

    private KakaoResponse convertKakao(String json) {
        try {
            return new KakaoResponse(objectMapper.readValue(json, KakaoResponseDto.class));
        }
        catch(JsonProcessingException e) {
            throw new OAuth2UserInfoException("Kakao 사용자 정보 변환에 실패했습니다.", e);
        }
    }

    private NaverResponse convertNaver(String json) {
        try {
            return new NaverResponse(objectMapper.readValue(json, NaverResponseDto.class));
        }
        catch(JsonProcessingException e) {
            throw new OAuth2UserInfoException("Naver 사용자 정보 변환에 실패했습니다.", e);
        }

    }
}
