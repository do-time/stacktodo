package io.app.stacktodobe.oauth;

import io.app.stacktodobe.member.adapter.out.persistence.repository.MemberRepository;
import io.app.stacktodobe.utils.E2eTest;
import io.app.stacktodobe.utils.testfixture.TestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@E2eTest
@DisplayName("POST /oauth2/authorization/google")
@TestPropertySource(properties = {
    "spring.security.oauth2.client.registration.google.client-id=test-client-id",
    "spring.security.oauth2.client.registration.google.client-secret=test-client-secret"
})
class POST_specs {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 구글_oauth_client_정상_요청시_200_ok를_반환한다(
            @Autowired TestFixture testFixture
    ) {
        // arrange
        // act
        ResponseEntity<Void> response = testFixture.client()
                .postForEntity("/oauth2/authorization/google", null, Void.class);

        // assert
        System.out.println("response = " + response);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void 구글_oauth_인증_엔드포인트_접근시_적절한_응답을_반환한다(
            @Autowired TestFixture testFixture
    ) {
        // arrange & act
        ResponseEntity<Void> response = testFixture.client()
                .getForEntity("/oauth2/authorization/google", Void.class);

        // assert
        assertThat(response.getStatusCode()).isIn(HttpStatus.OK, HttpStatus.FOUND);
    }

    @Test
    void 구글_oauth_콜백_엔드포인트_접근시_적절한_응답을_반환한다(
            @Autowired TestFixture testFixture
    ) {
        // arrange & act
        ResponseEntity<Void> response = testFixture.client()
                .getForEntity("/login/oauth2/code/google", Void.class);

        // assert
        // OAuth 콜백은 인증이 필요하므로 적절한 상태 코드를 반환해야 함
        assertThat(response.getStatusCode()).isIn(HttpStatus.OK, HttpStatus.FOUND, HttpStatus.UNAUTHORIZED);
    }

    @Test
    void 구글_oauth_로그인_실패시_적절한_에러_응답을_반환한다(
            @Autowired TestFixture testFixture
    ) {
        // arrange - 잘못된 OAuth 요청
        String invalidOAuthRequest = "/oauth2/authorization/invalid-provider";

        // act
        ResponseEntity<Void> response = testFixture.client()
                .getForEntity(invalidOAuthRequest, Void.class);

        // assert
        assertThat(response.getStatusCode()).isIn(HttpStatus.BAD_REQUEST, HttpStatus.NOT_FOUND, HttpStatus.UNAUTHORIZED);
    }


    // 헬퍼 메서드들
    private boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    private boolean isValidProvider(String provider) {
        return "google".equals(provider) || "github".equals(provider);
    }
}
