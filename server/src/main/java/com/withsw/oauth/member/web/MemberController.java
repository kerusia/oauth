package com.withsw.oauth.member.web;

import com.withsw.oauth.common.auth.JwtTokenProvider;
import com.withsw.oauth.member.domain.Member;
import com.withsw.oauth.member.dto.MemberCreateDto;
import com.withsw.oauth.member.dto.MemberLoginDto;
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
}

