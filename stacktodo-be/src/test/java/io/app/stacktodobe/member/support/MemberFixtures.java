package io.app.stacktodobe.member.support;

import io.app.stacktodobe.member.adapter.in.web.dto.AccessTokenCarrier;
import io.app.stacktodobe.member.adapter.in.web.dto.IssueTokenCommand;
import io.app.stacktodobe.member.adapter.in.web.dto.MemberCreateCommand;
import io.app.stacktodobe.member.adapter.in.web.dto.MemberView;
import org.springframework.boot.test.web.client.LocalHostUriTemplateHandler;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static io.app.stacktodobe.utils.TestSourceGenerator.*;
import static io.app.stacktodobe.utils.TestSourceGenerator.generatePhoneNumber;
import static io.app.stacktodobe.utils.TestSourceGenerator.generateProfileImage;
import static java.util.Objects.requireNonNull;

public class MemberFixtures {

    private final TestRestTemplate client;

    private MemberFixtures(TestRestTemplate client) {
        this.client = client;
    }

    public TestRestTemplate getClient() {
        return client;
    }

    public Long createMemberAndGetMemberId() {
        String email = generateEmail();
        String password = generatePassword();
        createMember(email, password);

        String accessToken = issueToken(email, password);

        setHeaderToken(accessToken);

        ResponseEntity<MemberView> memberResponse = client.getForEntity(
                "/api/v1/members/me",
                MemberView.class
        );
        return requireNonNull(memberResponse.getBody()).id();
    }

    private String issueToken(String email, String password) {
        ResponseEntity<AccessTokenCarrier> response = client.postForEntity(
                "/api/v1/members/issueToken",
                new IssueTokenCommand(email, password),
                AccessTokenCarrier.class);

        String accessToken = requireNonNull(response.getBody()).accessToken();
        return accessToken;
    }

    private void createMember(String email, String password) {
        var command = new MemberCreateCommand(
                email,
                password,
                generateNickname(),
                generateProfileImage(),
                generatePhoneNumber()
        );
        client.postForEntity(
                "/api/v1/members/signup",
                command,
                Void.class
        );
    }

    public static MemberFixtures create(Environment environment) {
        var client = new TestRestTemplate();
        var uriTemplateRenderer = new LocalHostUriTemplateHandler(environment);
        client.setUriTemplateHandler(uriTemplateRenderer);
        return new MemberFixtures(client);
    }

    private void setHeaderToken(String accessToken) {
        client.getRestTemplate().getInterceptors().add((request1, body, execution) -> {
            request1.getHeaders().add("Authorization", "Bearer " + accessToken);
            return execution.execute(request1, body);
        });
    }
}
