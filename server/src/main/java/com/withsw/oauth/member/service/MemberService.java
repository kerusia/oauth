package com.withsw.oauth.member.service;

import com.withsw.oauth.member.domain.Member;
import com.withsw.oauth.member.dto.MemberCreateDto;
import com.withsw.oauth.member.dto.MemberLoginDto;
import com.withsw.oauth.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member create(MemberCreateDto memberCreateDto) {
        Member member = Member.builder()
                .uuid(UUID.randomUUID().toString())
                .email(memberCreateDto.getEmail())
                .password(passwordEncoder.encode(memberCreateDto.getPassword()))
                .build();

        memberRepository.save(member);

        return member;
    }

    public Member login(MemberLoginDto memberLoginDto) {
        Optional<Member> optionalMember = memberRepository.findByEmail(memberLoginDto.getEmail());
        if(optionalMember.isEmpty()) {
            throw new IllegalArgumentException("이메일이 존재하지 않습니다.");
        }

        Member member = optionalMember.get();
        if(!passwordEncoder.matches(memberLoginDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return member;
    }
}
