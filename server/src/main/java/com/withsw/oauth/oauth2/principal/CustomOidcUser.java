package com.withsw.oauth.oauth2.principal;

import com.withsw.oauth.member.domain.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.util.Collection;

@Getter
public class CustomOidcUser extends DefaultOidcUser {
    private final Member member;

    public CustomOidcUser(Collection<? extends GrantedAuthority> authorities, Member member, OidcIdToken idToken) {
        super(authorities, idToken);
        this.member = member;
    }
}
