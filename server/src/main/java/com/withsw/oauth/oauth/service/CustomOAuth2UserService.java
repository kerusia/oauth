package com.withsw.oauth.oauth.service;

import com.withsw.oauth.member.domain.Member;
import com.withsw.oauth.member.domain.SocialType;
import com.withsw.oauth.member.service.MemberService;
import com.withsw.oauth.oauth.dto.GoogleResponse;
import com.withsw.oauth.oauth.dto.KakaoResponse;
import com.withsw.oauth.oauth.dto.NaverResponse;
import com.withsw.oauth.oauth.dto.OAuth2Response;
import com.withsw.oauth.oauth.principal.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberService memberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = new HashMap<>(oauth2User.getAttributes());

        OAuth2Response response = null;
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        RegistrationType registrationType = RegistrationType.valueOf(registrationId);
        switch(registrationType) {
            case google -> response = new GoogleResponse(oauth2User.getAttributes());
            case kakao -> response = new KakaoResponse(oauth2User.getAttributes());
            case naver -> {
                response = new NaverResponse(oauth2User.getAttributes());
                attributes.put("id", response.getProviderId());
            }
        }

        if(response == null) {
            throw new NullPointerException("OAuth2User is null");
        }

        // 회원조회
        Member member = memberService.getMemberBySocialId(response.getProviderId());

        // 미가입 상태이면 회원가입
        if(member == null) {
            member = memberService.createOauth(response.getProviderId(), response.getEmail(), response.getNickname(), SocialType.valueOf(response.getProviderName()));
        }

        return new CustomOAuth2User(oauth2User.getAuthorities(), attributes, oauth2User.getName(), member);
    }
}
