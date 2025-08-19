package io.app.stacktodobe.issuetoken;

import io.app.stacktodobe.member.presentation.command.AccessTokenCarrier;
import io.app.stacktodobe.member.presentation.command.IssueTokenCommand;
import io.app.stacktodobe.member.presentation.command.MemberCreateCommand;
import io.app.stacktodobe.utils.E2eTest;
import io.app.stacktodobe.utils.testfixture.TestFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static io.app.stacktodobe.utils.TestSourceGenerator.*;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@E2eTest
class POST_specs {

    @Test
    void 토큰발급_정상요청시_200_OK_를_반환한다(
            @Autowired TestFixture fixture
            ) {
        // arrange
        String email = generateEmail();
        String password = "ValidPassword123!";

        fixture.client().postForEntity(
                "/api/v1/members/signup",
                new MemberCreateCommand(
                        email,
                        password,
                        generateNickname(),
                        generateProfileImage(),
                        generatePhoneNumber()),
                Void.class
        );

        var command = new IssueTokenCommand(email, password);

        // act
         var response = fixture.client().postForEntity(
                 "/api/v1/members/issueToken",
                    command,
                    Void.class
            );

         // assert
        assertThat(response.getStatusCode().value())
                .isEqualTo(200);

    }

    @Test
    void 올바르게_요청하면_접근_토큰을_반환한다(
            @Autowired TestFixture fixture
            ) {
        // arrange
        String email = "test@example.com";
        String password = "ValidPassword123!";

        fixture.client().postForEntity(
                "/api/v1/members/signup",
                new MemberCreateCommand(
                        email,
                        password,
                        generateNickname(),
                        generateProfileImage(),
                        generatePhoneNumber()),
                Void.class
        );

        var command = new IssueTokenCommand(email, password);


        // act
        ResponseEntity<AccessTokenCarrier> response = fixture.client().postForEntity(
                "/api/v1/members/issueToken",
                command,
                AccessTokenCarrier.class
        );

        // assert
        assertThat(response.getBody()).isNotNull();
        assertThat(requireNonNull(response.getBody()).accessToken()).isNotNull();
    }

}
