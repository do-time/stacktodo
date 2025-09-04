package io.app.stacktodobe.member;


import io.app.stacktodobe.member.adapter.in.web.dto.CreateMemberDto;
import io.app.stacktodobe.member.adapter.in.web.dto.MemberView;
import io.app.stacktodobe.member.support.MemberFixtures;
import io.app.stacktodobe.utils.E2eTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.app.stacktodobe.utils.TestSourceGenerator.*;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@E2eTest
@DisplayName("GET /api/v1/members")
public class GET_specs {

    @Test
    void 정상_조회시_200_ok를_반환한다_단건조회(
            @Autowired MemberFixtures memberFixtures
    ) {
        // arrange
        String email = generateEmail();
        String password = generatePassword();
        memberFixtures.createMemberAndSetMemberAsDefaultUser(email, password);

        // act
        var response = memberFixtures.getClient()
                .getForEntity("/api/v1/members/me"
                        , Void.class);

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void 인증없이_조회시_401_unauthorized를_반환한다_단건조회(
            @Autowired MemberFixtures memberFixtures
    ) {
        // arrange
        String email = generateEmail();
        String password = generatePassword();
        memberFixtures.createMember(email, password);

        // act
        var response = memberFixtures.getClient()
                .getForEntity("/api/v1/members/me"
                        , Void.class);
        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(401);
    }

    @Test
    void 정상조회_시에_올바른정보를_반환한다(
        @Autowired MemberFixtures memberFixtures
    ) {
        // arrange
        String email = generateEmail();
        String password = generatePassword();
        String username = generateUsername();
        String profileImage = generateProfileImage();
        String phoneNumber = generatePhoneNumber();

        memberFixtures.getClient()
                .postForEntity("/api/v1/members/signup",
                        new CreateMemberDto(
                                email,
                                password,
                                username,
                                profileImage,
                                phoneNumber
                        ),
                        Void.class
                );
        // arrange
        memberFixtures.setHeaderToken(memberFixtures.issueToken(email, password));

        // act
        var response = memberFixtures.getClient()
                .getForEntity("/api/v1/members/me"
                        , MemberView.class);
        // assert
        var responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(requireNonNull(responseBody).email()).isEqualTo(email);
        assertThat(responseBody.username()).isEqualTo(username);
        assertThat(responseBody.profileImage()).isEqualTo(profileImage);
        assertThat(responseBody.phoneNumber()).isEqualTo(phoneNumber);
    }
}
