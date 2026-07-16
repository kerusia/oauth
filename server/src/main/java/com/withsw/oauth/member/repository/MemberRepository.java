package com.withsw.oauth.member.repository;

import com.withsw.oauth.member.domain.Member;
import com.withsw.oauth.member.dto.MemberLoginDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
}
