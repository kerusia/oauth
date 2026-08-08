package com.withsw.oauth.oauth2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.withsw.oauth.member.domain.Member;
import com.withsw.oauth.member.domain.Role;
import com.withsw.oauth.member.domain.SocialType;
import com.withsw.oauth.member.dto.GoogleResponseDto;
import com.withsw.oauth.member.service.MemberService;
import com.withsw.oauth.oauth2.dto.GoogleResponse;
import com.withsw.oauth.oauth2.dto.OAuth2Response;
import com.withsw.oauth.oauth2.principal.CustomOidcUser;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClient;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomOidcUserService extends OidcUserService {

    private final MemberService memberService;
    private final ObjectMapper objectMapper;
    private final RestClient restClient = RestClient.create();

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        Assert.notNull(userRequest, "userRequest cannot be null");

        try {
            OidcUser oidcUser = super.loadUser(userRequest);

            String registrationId = userRequest.getClientRegistration().getRegistrationId();
            RegistrationType registrationType = RegistrationType.valueOf(registrationId);

            // 사용자 정보 조회
            OAuth2Response response = getOAuth2Response(registrationType, oidcUser);

            // 회원조회
            Member member = memberService.getMemberBySocialTypeAndSocialId(SocialType.valueOf(registrationId.toUpperCase()), response.getProviderId());

            // 미가입 상태이면 회원가입
            if(member == null) {
                member = memberService.createOauth(response.getProviderId(), response.getEmail(), response.getNickname(), SocialType.valueOf(response.getProviderName()));
            }

            // 권한 생성
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + Role.USER.name()));

            return new CustomOidcUser(authorities, member, userRequest.getIdToken());
        }
        catch(OAuth2UserInfoException e) {
            throw new OAuth2AuthenticationException(new OAuth2Error("oauth2_user_info_error"), "OAuth 사용자 정보 처리에 실패했습니다.", e);
        }
    }

    private static @NonNull OAuth2Response getOAuth2Response(RegistrationType registrationType, OidcUser oidcUser) {
        OAuth2Response response = null;
        switch(registrationType) {
            case RegistrationType.google -> {
                GoogleResponseDto googleResponseDto = new GoogleResponseDto(oidcUser.getSubject(), oidcUser.getEmail(), oidcUser.getEmailVerified(), oidcUser.getPicture(), oidcUser.getAttribute("name"));
                response = new GoogleResponse(googleResponseDto);
            }
            default -> throw new OAuth2AuthenticationException("OAuth2User is null");
        }
        return response;
    }
}
