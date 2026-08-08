package com.withsw.oauth.oauth2.service;

import com.withsw.oauth.common.auth.JwtTokenProvider;
import com.withsw.oauth.member.domain.Member;
import com.withsw.oauth.oauth2.principal.CustomOAuth2User;
import com.withsw.oauth.oauth2.principal.CustomOidcUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Member member;
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomOAuth2User oauth2User) {
            member = oauth2User.getMember();
        }
        else if(principal instanceof CustomOidcUser oidcUser){
            member = oidcUser.getMember();
        }
        else {
            throw new OAuth2AuthenticationException("Unsupported principal type" + principal.getClass().getName());
        }

        String accessToken = jwtTokenProvider.createToken(member.getEmail(), member.getRole().name());

        // 쿼리스트링으로 토큰 전달
        response.sendRedirect("http://localhost:3000?token=" + accessToken);

        // 쿠키로 전달
        /*ResponseCookie cookie = ResponseCookie.from("token", accessToken)
                .httpOnly(true)
                .secure(true)
                .sameSite(Cookie.SameSite.LAX.name())
                .path("/")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.sendRedirect("http://localhost:3000");*/
    }
}
