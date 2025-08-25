package io.app.stacktodobe.member.me;

import io.app.stacktodobe.member.presentation.view.MemberMeView;
import io.app.stacktodobe.utils.E2eTest;
import io.app.stacktodobe.utils.testfixture.TestFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@E2eTest
public class GET_specs {

    @Test
    void 회원정보_조회_정상요청시_200_OK_를_반환한다(
            @Autowired TestFixture fixture
    ) {
        // arrange
        fixture.createMemberAndSetDefaultLogin();

        // act
        var response = fixture.client().getForEntity(
                "/api/v1/members/me",
                Void.class
        );

        // assert
        assertThat(response.getStatusCode().value())
                .isEqualTo(200);
    }

    @Test
    void 회원정보_조회_요청시_회원_정보를_정상_반환한다(
            @Autowired TestFixture fixture
    ) {
        // arrange
        fixture.createMemberAndSetDefaultLogin();


        // act
        ResponseEntity<MemberMeView> response = fixture.client()
                .getForEntity(
                        "/api/v1/members/me",
                        MemberMeView.class
                );

        // assert
        assertThat(response.getStatusCode().value())
                .isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(requireNonNull(response.getBody()).email()).isNotNull();
    }
}
