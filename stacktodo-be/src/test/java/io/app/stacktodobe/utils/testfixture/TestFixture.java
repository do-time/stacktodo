package io.app.stacktodobe.utils.testfixture;


import io.app.stacktodobe.member.adapter.in.web.dto.AccessTokenCarrier;
import io.app.stacktodobe.member.adapter.in.web.dto.CreateMemberDto;
import io.app.stacktodobe.member.adapter.in.web.dto.IssueTokenCommand;
import org.springframework.boot.test.web.client.LocalHostUriTemplateHandler;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static io.app.stacktodobe.utils.TestSourceGenerator.*;
import static java.util.Objects.requireNonNull;

public record TestFixture(
        TestRestTemplate client
) {
    public static TestFixture create(Environment environment) {
        var client = new TestRestTemplate();
        var uriTemplateRenderer = new LocalHostUriTemplateHandler(environment);
        client.setUriTemplateHandler(uriTemplateRenderer);
        return new TestFixture(client);
    }

    public void createMemberAndSetDefaultUser() {
        String email = generateEmail();
        String password = generatePassword();
        var request = new CreateMemberDto(
                email,
                password,
                generateNickname(),
                generateProfileImage(),
                generatePhoneNumber()
        );
        client.postForEntity("/api/v1/members", request, Void.class);

        // issueToken
        var issueTokenCommand = new IssueTokenCommand(
                email,
                password
        );

        ResponseEntity<AccessTokenCarrier> response = client.postForEntity("/api/v1/members/tokens", issueTokenCommand, AccessTokenCarrier.class);
        String accessToken = requireNonNull(response.getBody()).accessToken();

        setHeaderToken(accessToken);
    }

    private void setHeaderToken(String accessToken) {
        client.getRestTemplate().getInterceptors().add((request1, body, execution) -> {
            request1.getHeaders().add("Authorization", "Bearer " + accessToken);
            return execution.execute(request1, body);
        });
    }
}
