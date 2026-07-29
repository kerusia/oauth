package com.withsw.oauth.member.web;

import com.withsw.oauth.common.auth.JwtTokenProvider;
import com.withsw.oauth.member.domain.Member;
import com.withsw.oauth.member.domain.SocialType;
import com.withsw.oauth.member.dto.*;
import com.withsw.oauth.member.service.GoogleService;
import com.withsw.oauth.member.service.KakaoService;
import com.withsw.oauth.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberController {
    private final MemberService memberService;
    private final GoogleService googleService;
    private final KakaoService kakaoService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/create")
    public ResponseEntity<?> createMember(@RequestBody MemberCreateDto memberCreateDto) {
        Member member = memberService.create(memberCreateDto);
        return new ResponseEntity<>(member.getId(), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberLoginDto memberLoginDto) {
        // 검증
        Member member = memberService.login(memberLoginDto);

        if(member == null) {
            throw new RuntimeException("사용자 인증에 실패하였습니다.");
        }

        // JWT 생성
        String jwtToken = jwtTokenProvider.createToken(member.getEmail(), member.getRole().name());

        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("uuid", member.getUuid());
        loginInfo.put("token", jwtToken);

        return new ResponseEntity<>(loginInfo, HttpStatus.OK);
    }

    @PostMapping("/google/login")
    public ResponseEntity<?> googleLogin(@RequestBody RedirectDto redirectDto) {

        // 액세스토큰 발급
        AccessTokenDto accessTokenDto = googleService.getAccessToken(redirectDto.getCode());

        // 구글 프로필 조회
        GoogleProfileDto googleProfileDto = googleService.getProfile(accessTokenDto.getAccessToken(), accessTokenDto.getTokenType());

        // 사용자 조회
        Member originalMember = memberService.getMemberBySocialId(googleProfileDto.getSub());

        // 미가입 상태이면 회원가입
        if(originalMember == null) {
            originalMember = memberService.createOauth(googleProfileDto.getSub(), googleProfileDto.getEmail(), googleProfileDto.getName(), SocialType.GOOGLE);
        }

        // 토큰 발급
        String jwtToken = jwtTokenProvider.createToken(originalMember.getEmail(), originalMember.getRole().name());

        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("uuid", originalMember.getUuid());
        loginInfo.put("token", jwtToken);

        return new ResponseEntity<>(loginInfo, HttpStatus.OK);
    }

    @PostMapping("/kakao/login")
    public ResponseEntity<?> kakaoLogin(@RequestBody RedirectDto redirectDto) {

        // 액세스토큰 발급
        AccessTokenDto accessTokenDto = kakaoService.getAccessToken(redirectDto.getCode());

        // 구글 프로필 조회
        KakaoResponseDto kakaoResponseDto = kakaoService.getProfile(accessTokenDto.getAccessToken(), accessTokenDto.getTokenType());

        // 사용자 조회
        Member originalMember = memberService.getMemberBySocialId(kakaoResponseDto.id());

        // 미가입 상태이면 회원가입
        if(originalMember == null) {
            originalMember = memberService.createOauth(kakaoResponseDto.id(), kakaoResponseDto.kakaoAccount().email(), kakaoResponseDto.kakaoAccount().profile().nickname(), SocialType.KAKAO);
        }

        // 토큰 발급
        String jwtToken = jwtTokenProvider.createToken(originalMember.getEmail(), originalMember.getRole().name());

        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("uuid", originalMember.getUuid());
        loginInfo.put("token", jwtToken);

        return new ResponseEntity<>(loginInfo, HttpStatus.OK);
    }
}

