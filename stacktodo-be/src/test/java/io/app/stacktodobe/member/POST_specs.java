package io.app.stacktodobe.member;

import io.app.stacktodobe.member.presentation.command.MemberCreateCommand;
import io.app.stacktodobe.utils.E2eTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static io.app.stacktodobe.utils.TestSourceGenerator.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@E2eTest
public class POST_specs {

    @Test
    void 회원가입_정상요청시_200_ok를_반환한다(
            @Autowired TestRestTemplate testRestTemplate
    ) {
        //arrange
        var command = new MemberCreateCommand(
                generateEmail(),
                generatePassword(),
                generateName(),
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
                .isEqualTo(200);


    }
}
