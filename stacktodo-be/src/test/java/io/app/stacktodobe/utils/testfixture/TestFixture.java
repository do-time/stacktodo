package io.app.stacktodobe.utils.testfixture;


import io.app.stacktodobe.member.presentation.command.AccessTokenCarrier;
import io.app.stacktodobe.member.presentation.command.IssueTokenCommand;
import io.app.stacktodobe.member.presentation.command.MemberCreateCommand;
import org.springframework.boot.test.web.client.LocalHostUriTemplateHandler;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;

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

    public void createMemberAndSetDefaultLogin() {
        String email = generateEmail();
        String password = generatePassword();
        memberCreate(email, password);

        issueTokenAndSetToHeader(email, password);
    }

    private void issueTokenAndSetToHeader(String email, String password) {
        var command = new IssueTokenCommand(email, password);

        var response = client.postForEntity(
                "/api/v1/members/issueToken",
                command,
                AccessTokenCarrier.class
        );

        String token = requireNonNull(response.getBody()).accessToken();

        client.getRestTemplate().getInterceptors().add(
                (request, body, execution) -> {
                    request.getHeaders().add("Authorization", "Bearer " + token);
                    return execution.execute(request, body);
                }
        );
    }

    private void memberCreate(String email, String password) {
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


}
