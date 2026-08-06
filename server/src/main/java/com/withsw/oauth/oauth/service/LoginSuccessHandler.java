package com.withsw.oauth.oauth.service;

import com.withsw.oauth.common.auth.JwtTokenProvider;
import com.withsw.oauth.member.domain.Member;
import com.withsw.oauth.oauth.principal.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();
        Member member = user.getMember();

        String accessToken = jwtTokenProvider.createToken(member.getEmail(), member.getRole().name());

        // 쿼리스트링으로 토큰 전달
        response.sendRedirect("http://localhost:3000?token=" + accessToken);

        // 쿠키로 전달
        /*Cookie cookie = new Cookie("token", accessToken);
        response.addCookie(cookie);
        response.sendRedirect("http://localhost:3000");*/
    }
}
