package io.app.stacktodobe.infrastructure.config.oauth;

import io.app.stacktodobe.member.application.port.in.command.OAuthMemberCreateCommand;
import io.app.stacktodobe.member.adapter.out.persistence.entity.MemberEntity;
import io.app.stacktodobe.member.adapter.out.persistence.repository.MemberRepository;
import io.app.stacktodobe.member.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    public PrincipalOauth2UserService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);


        /** TODO
         * OAuth2User 토큰 발급
         * JWT 토큰을 직접 발급 받게 할것인지 아니면 google로 부터 받은 토큰을 사용할지 결정해야함
         * redirect url을 통해 토큰 발급이 자동으로 이루어지도록 해야함.
         * SUCCESS_REDIRECT_URL = "http://localhost:8080/oauth2/issueToken"
         *
         * userRequest.getAccessToken().getTokenValue();
         */


        // 소셜 로그인 provider
        String provider = userRequest.getClientRegistration()
                .getRegistrationId();
        String provideId = oauth2User.getAttribute("sub");
        String email = oauth2User.getAttribute("email");
        String username = oauth2User.getAttribute("name");
        String role = "ROLE_USER";

        var memberCommand = new OAuthMemberCreateCommand(
                email,
                username,
                oauth2User.getAttribute("picture"),
                oauth2User.getAttribute("phone_number"),
                provider,
                provideId,
                role
        );
        Member member = Member.createMemberByOauth(memberCommand);

        Optional<MemberEntity> user = memberRepository.findByEmail(email);
        if (user.isEmpty()) {
            log.info("First time social login. Creating new user.");

            MemberEntity memberEntity = MemberEntity.domainToEntity(member, "NO_PWD");

            // oauth2User.getAttributes() 정보로 회원가입 처리
            memberRepository.save(memberEntity);

            return new PrincipalDetails(member, oauth2User.getAttributes());
        } else {
            return new PrincipalDetails(member, oauth2User.getAttributes());
        }
    }
}
