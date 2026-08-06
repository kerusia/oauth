package com.withsw.oauth.oauth.principal;

import com.withsw.oauth.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {
    private final Member member;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey, Member member) {
        super(authorities, attributes, nameAttributeKey);
        this.member = member;
    }
}
