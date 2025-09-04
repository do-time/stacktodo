package io.app.stacktodobe.infrastructure.config.oauth;

import io.app.stacktodobe.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class PrincipalDetails implements UserDetails, OAuth2User {
    @Getter
    private Member member;
    private String username;
    private String password;
    private Map<String, Object> attributes;

    //OAuth 로그인
    public PrincipalDetails(Member member, Map<String,Object> attributes) {
        this.member = member;
    }

    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }


//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return member.getAuthorities();
//    }

}
