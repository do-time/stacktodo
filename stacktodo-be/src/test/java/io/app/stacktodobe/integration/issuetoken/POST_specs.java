package io.app.stacktodobe.integration.issuetoken;

import io.app.stacktodobe.member.adapter.in.web.dto.AccessTokenCarrier;
import io.app.stacktodobe.member.application.port.in.command.IssueTokenCommand;
import io.app.stacktodobe.member.application.port.in.command.MemberCreateCommand;
import io.app.stacktodobe.integration.member.support.MemberFixtures;
import io.app.stacktodobe.utils.E2eTest;
import io.app.stacktodobe.utils.testfixture.TestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static io.app.stacktodobe.utils.TestSourceGenerator.*;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@E2eTest
@DisplayName("POST /api/v1/members/issueToken")
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
                        null,
                        generateUsername(),
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
        String email = generateEmail(); // 테스트 격리를 위해 동적 생성
        String password = "ValidPassword123!";

        fixture.client().postForEntity(
                "/api/v1/members/signup",
                new MemberCreateCommand(
                        email,
                        password,
                        null,
                        generateUsername(),
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

    @Test
    void 존재하지_않는_이메일로_요청시_400_BadRequest를_반환한다(
            @Autowired TestFixture fixture
            ) {
        // arrange
        String nonExistentEmail = "nonexistent@example.com";
        String password = "ValidPassword123!";
        var command = new IssueTokenCommand(nonExistentEmail, password);

        // act
        var response = fixture.client().postForEntity(
                "/api/v1/members/issueToken",
                command,
                Void.class
        );

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void 잘못된_비밀번호로_요청시_400_BadRequest를_반환한다(
            @Autowired MemberFixtures fixture
    ) {
        // arrange
        String email = generateEmail(); // 테스트 격리를 위해 동적 생성
        String correctPassword = "ValidPassword123!";
        String wrongPassword = "WrongPassword123!";

        fixture.createMember(email, correctPassword);

        var command = new IssueTokenCommand(email, wrongPassword);

        // act
        var response = fixture.getClient().postForEntity(
                "/api/v1/members/issueToken",
                command,
                Void.class
        );

        // assert
        assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 이메일이_null일_때_400_BadRequest를_반환한다(
            @Autowired TestFixture fixture
            ) {
        // arrange
        String password = "ValidPassword123!";
        var command = new IssueTokenCommand(null, password);

        // act
        var response = fixture.client().postForEntity(
                "/api/v1/members/issueToken",
                command,
                Void.class
        );

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void 이메일이_빈_문자열일_때_400_BadRequest를_반환한다(
            @Autowired TestFixture fixture
            ) {
        // arrange
        String emptyEmail = "";
        String password = "ValidPassword123!";
        var command = new IssueTokenCommand(emptyEmail, password);

        // act
        var response = fixture.client().postForEntity(
                "/api/v1/members/issueToken",
                command,
                Void.class
        );

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void 비밀번호가_null일_때_400_BadRequest를_반환한다(
            @Autowired TestFixture fixture
            ) {
        // arrange
        String email = generateEmail();
        var command = new IssueTokenCommand(email, null);

        // act
        var response = fixture.client().postForEntity(
                "/api/v1/members/issueToken",
                command,
                Void.class
        );

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void 비밀번호가_빈_문자열일_때_400_BadRequest를_반환한다(
            @Autowired TestFixture fixture
            ) {
        // arrange
        String email = generateEmail();
        String emptyPassword = "";
        var command = new IssueTokenCommand(email, emptyPassword);

        // act
        var response = fixture.client().postForEntity(
                "/api/v1/members/issueToken",
                command,
                Void.class
        );

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void 매우_긴_이메일로_요청시_400_BadRequest를_반환한다(
            @Autowired TestFixture fixture
            ) {
        // arrange
        String veryLongEmail = "a".repeat(100) + "@example.com";
        String password = "ValidPassword123!";
        var command = new IssueTokenCommand(veryLongEmail, password);

        // act
        var response = fixture.client().postForEntity(
                "/api/v1/members/issueToken",
                command,
                Void.class
        );

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void 특수문자가_포함된_이메일로_요청시_400_BadRequest를_반환한다(
            @Autowired TestFixture fixture
            ) {
        // arrange
        String emailWithSpecialChars = "test<script>alert('xss')</script>@example.com";
        String password = "ValidPassword123!";
        var command = new IssueTokenCommand(emailWithSpecialChars, password);

        // act
        var response = fixture.client().postForEntity(
                "/api/v1/members/issueToken",
                command,
                Void.class
        );

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void SQL_인젝션_시도시_400_BadRequest를_반환한다(
            @Autowired TestFixture fixture
            ) {
        // arrange
        String sqlInjectionEmail = "'; DROP TABLE members; --";
        String password = "ValidPassword123!";
        var command = new IssueTokenCommand(sqlInjectionEmail, password);

        // act
        var response = fixture.client().postForEntity(
                "/api/v1/members/issueToken",
                command,
                Void.class
        );

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void 발급된_토큰이_올바른_JWT_형식인지_검증한다(
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
                        null,
                        generateUsername(),
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
        String accessToken = requireNonNull(response.getBody()).accessToken();
        assertThat(accessToken).isNotNull();

        // JWT 형식 검증 (header.payload.signature)
        String[] parts = accessToken.split("\\.");
        assertThat(parts).hasSize(3);
        assertThat(parts[0]).isNotEmpty(); // header
        assertThat(parts[1]).isNotEmpty(); // payload
        assertThat(parts[2]).isNotEmpty(); // signature
    }

    @Test
    void 잘못된_이메일_형식_at_기호_없음_400_BadRequest를_반환한다(
            @Autowired TestFixture fixture
            ) {
        // arrange
        String invalidEmail = "invalidemail.com"; // @ 없음
        String password = "ValidPassword123!";
        var command = new IssueTokenCommand(invalidEmail, password);

        // act
        var response = fixture.client().postForEntity(
                "/api/v1/members/issueToken",
                command,
                Void.class
        );

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void 잘못된_이메일_형식_도메인_없음_400_BadRequest를_반환한다(
            @Autowired TestFixture fixture
            ) {
        // arrange
        String invalidEmail = "test@"; // 도메인 없음
        String password = "ValidPassword123!";
        var command = new IssueTokenCommand(invalidEmail, password);

        // act
        var response = fixture.client().postForEntity(
                "/api/v1/members/issueToken",
                command,
                Void.class
        );

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void 화이트스페이스만_있는_이메일로_요청시_400_BadRequest를_반환한다(
            @Autowired TestFixture fixture
            ) {
        // arrange
        String whitespaceEmail = "   ";
        String password = "ValidPassword123!";
        var command = new IssueTokenCommand(whitespaceEmail, password);

        // act
        var response = fixture.client().postForEntity(
                "/api/v1/members/issueToken",
                command,
                Void.class
        );

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void 화이트스페이스만_있는_비밀번호로_요청시_400_BadRequest를_반환한다(
            @Autowired TestFixture fixture
            ) {
        // arrange
        String email = generateEmail();
        String whitespacePassword = "   ";
        var command = new IssueTokenCommand(email, whitespacePassword);

        // act
        var response = fixture.client().postForEntity(
                "/api/v1/members/issueToken",
                command,
                Void.class
        );

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void 비밀번호가_최대_길이를_초과할_때_400_BadRequest를_반환한다(
            @Autowired TestFixture fixture
            ) {
        // arrange
        String email = generateEmail();
        String tooLongPassword = "a".repeat(101); // 101자
        var command = new IssueTokenCommand(email, tooLongPassword);

        // act
        var response = fixture.client().postForEntity(
                "/api/v1/members/issueToken",
                command,
                Void.class
        );

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void 동일_사용자가_여러_번_토큰을_발급받을_수_있다(
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
                        null,
                        generateUsername(),
                        generateProfileImage(),
                        generatePhoneNumber()),
                Void.class
        );

        var command = new IssueTokenCommand(email, password);

        // act - 첫 번째 토큰 발급
        ResponseEntity<AccessTokenCarrier> firstResponse = fixture.client().postForEntity(
                "/api/v1/members/issueToken",
                command,
                AccessTokenCarrier.class
        );

        // act - 두 번째 토큰 발급
        ResponseEntity<AccessTokenCarrier> secondResponse = fixture.client().postForEntity(
                "/api/v1/members/issueToken",
                command,
                AccessTokenCarrier.class
        );

        // assert - 여러 번 발급 가능하며 모두 유효한 토큰임을 검증
        assertThat(firstResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(secondResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(firstResponse.getBody()).isNotNull();
        assertThat(secondResponse.getBody()).isNotNull();

        String firstToken = requireNonNull(firstResponse.getBody()).accessToken();
        String secondToken = requireNonNull(secondResponse.getBody()).accessToken();

        assertThat(firstToken).isNotNull();
        assertThat(secondToken).isNotNull();

        // JWT 형식 검증
        assertThat(firstToken.split("\\.")).hasSize(3);
        assertThat(secondToken.split("\\.")).hasSize(3);
    }

    @Test
    void 비밀번호_대소문자_구분_테스트(
            @Autowired TestFixture fixture
            ) {
        // arrange
        String email = generateEmail();
        String password = "ValidPassword123!";
        String wrongCasePassword = "validpassword123!"; // 소문자로 변경

        fixture.client().postForEntity(
                "/api/v1/members/signup",
                new MemberCreateCommand(
                        email,
                        password,
                        null,
                        generateUsername(),
                        generateProfileImage(),
                        generatePhoneNumber()),
                Void.class
        );

        var command = new IssueTokenCommand(email, wrongCasePassword);

        // act
        var response = fixture.client().postForEntity(
                "/api/v1/members/issueToken",
                command,
                Void.class
        );

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}
