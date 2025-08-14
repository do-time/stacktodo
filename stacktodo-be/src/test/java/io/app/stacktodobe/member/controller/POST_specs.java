package io.app.stacktodobe.member.controller;

import io.app.stacktodobe.member.presentation.command.MemberCreateCommand;
import io.app.stacktodobe.utils.E2eTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static io.app.stacktodobe.utils.TestSourceGenerator.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@E2eTest
public class POST_specs {

    @Test
    void 회원가입_정상요청시_204_NO_CONTENT_를_반환한다(
            @Autowired TestRestTemplate testRestTemplate
    ) {
        //arrange
        var command = new MemberCreateCommand(
                generateEmail(),
                generatePassword(),
                generateNickname(),
                generateProfileImage(),
                generatePhoneNumber()
        );

        //act
        var response = testRestTemplate.postForEntity(
                "/api/v1/members/signup",
                command,
                Void.class);
        //assert
        assertThat(response.getStatusCodeValue())
                .isEqualTo(204);
    }

    @ValueSource(strings = {
            "test@email...",
            "test.email.cc"
    })
    @ParameterizedTest
    void 잘못된_형식의_이메일요청시_400_bad_request(
            String email,
            @Autowired TestRestTemplate testRestTemplate
    ) {
        var command = new MemberCreateCommand(
                email,
         generatePassword(),
         generateNickname(),
         generateProfileImage(),
         generatePhoneNumber()
        );
    }

    @ValueSource(strings = {
            "1234",
            "short",
            "abcdefg",
            "abcdefghij"

    })
    @ParameterizedTest
    void 잘못된_형식의_패스워드_입력시_400_bad_request(
            String password,
            @Autowired TestRestTemplate testRestTemplate
    ) {
        //arrange
        var command = new MemberCreateCommand(
                generateEmail(),
                password,
                generateNickname(),
                generateProfileImage(),
                generatePhoneNumber()
        );

        //act
        var response = testRestTemplate.postForEntity(
                "/api/v1/members/signup",
                command,
                Void.class
        );

        //assert
        assertThat(response.getStatusCodeValue())
                .isEqualTo(400);
    }

}
