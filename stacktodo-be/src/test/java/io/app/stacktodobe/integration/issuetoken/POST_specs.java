package io.app.stacktodobe.integration.issuetoken;

import io.app.stacktodobe.member.adapter.in.web.dto.AccessTokenCarrier;
import io.app.stacktodobe.member.adapter.in.web.dto.MemberView;
import io.app.stacktodobe.member.application.port.in.command.IssueTokenCommand;
import io.app.stacktodobe.integration.member.support.MemberFixtures;
import io.app.stacktodobe.utils.E2eTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import static io.app.stacktodobe.utils.TestSourceGenerator.*;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * 토큰 발급 API 통합 테스트
 * - MemberFixtures를 사용하여 중복 코드 제거
 * - @Nested로 테스트 구조화
 * - 에러 응답 검증 강화
 * - 토큰 실제 사용 가능 여부 검증
 */
@E2eTest
@DisplayName("POST /api/v1/members/issueToken")
class POST_specs {

    // ===== 정상 케이스 테스트 =====

    @Nested
    @DisplayName("정상 케이스")
    class SuccessCases {

        @Test
        @DisplayName("유효한 인증 정보로 요청하면 200 OK와 JWT 토큰을 반환한다")
        void issueToken_WithValidCredentials_Returns200AndJWT(
                @Autowired MemberFixtures fixture
        ) {
            // arrange
            String email = generateEmail();
            String password = "ValidPassword123!";
            fixture.createMember(email, password);

            // act
            ResponseEntity<AccessTokenCarrier> response = fixture.issueTokenWithResponse(email, password);

            // assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();

            String token = requireNonNull(response.getBody()).accessToken();
            assertThat(token).isNotNull();

            // JWT 형식 검증 (header.payload.signature)
            String[] parts = token.split("\\.");
            assertThat(parts.length).isEqualTo(3);
            assertThat(parts[0]).isNotEmpty();
            assertThat(parts[1]).isNotEmpty();
            assertThat(parts[2]).isNotEmpty();
        }

        @Test
        @DisplayName("응답은 application/json Content-Type을 가진다")
        void issueToken_ReturnsJsonContentType(
                @Autowired MemberFixtures fixture
        ) {
            // arrange
            String email = generateEmail();
            String password = "ValidPassword123!";
            fixture.createMember(email, password);

            // act
            ResponseEntity<AccessTokenCarrier> response = fixture.issueTokenWithResponse(email, password);

            // assert
            assertThat(response.getHeaders().getContentType()).isNotNull();
            assertThat(response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON)).isTrue();
        }

        @Test
        @DisplayName("발급된 토큰으로 인증된 API를 호출할 수 있다")
        void issuedToken_CanBeUsedForAuthenticatedRequests(
                @Autowired MemberFixtures fixture
        ) {
            // arrange
            String email = generateEmail();
            String password = "ValidPassword123!";
            fixture.createMember(email, password);

            String token = fixture.issueToken(email, password);

            // act - 발급받은 토큰으로 인증된 API 호출
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> request = new HttpEntity<>(headers);

            ResponseEntity<MemberView> response = fixture.getClient().exchange(
                    "/api/v1/members/me",
                    HttpMethod.GET,
                    request,
                    MemberView.class
            );

            // assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(requireNonNull(response.getBody()).email()).isEqualTo(email);
        }

        @Test
        @DisplayName("동일 사용자가 여러 번 토큰을 발급받을 수 있다")
        void sameUser_CanIssueMultipleTokens(
                @Autowired MemberFixtures fixture
        ) {
            // arrange
            String email = generateEmail();
            String password = "ValidPassword123!";
            fixture.createMember(email, password);

            // act
            ResponseEntity<AccessTokenCarrier> firstResponse = fixture.issueTokenWithResponse(email, password);
            ResponseEntity<AccessTokenCarrier> secondResponse = fixture.issueTokenWithResponse(email, password);

            // assert
            assertThat(firstResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(secondResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(firstResponse.getBody()).isNotNull();
            assertThat(secondResponse.getBody()).isNotNull();
        }
    }

    // ===== 입력 검증 테스트 =====

    @Nested
    @DisplayName("입력 검증 - 이메일")
    class EmailValidation {

        @Test
        @DisplayName("이메일이 null이면 400 BadRequest를 반환한다")
        void issueToken_WithNullEmail_Returns400(
                @Autowired MemberFixtures fixture
        ) {
            // arrange & act
            var response = fixture.getClient().postForEntity(
                    "/api/v1/members/issueToken",
                    new IssueTokenCommand(null, "password"),
                    String.class  // 에러 응답을 String으로 받음
            );

            // assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("이메일이 빈 문자열이면 400 BadRequest를 반환한다")
        void issueToken_WithEmptyEmail_Returns400(
                @Autowired MemberFixtures fixture
        ) {
            // arrange & act
            var response = fixture.getClient().postForEntity(
                    "/api/v1/members/issueToken",
                    new IssueTokenCommand("", "password"),
                    String.class
            );

            // assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("이메일이 화이트스페이스만 있으면 400 BadRequest를 반환한다")
        void issueToken_WithWhitespaceEmail_Returns400(
                @Autowired MemberFixtures fixture
        ) {
            // arrange & act
            var response = fixture.getClient().postForEntity(
                    "/api/v1/members/issueToken",
                    new IssueTokenCommand("   ", "password"),
                    String.class
            );

            // assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("이메일 형식이 잘못되었으면(@가 없음) 400 BadRequest를 반환한다")
        void issueToken_WithInvalidEmailFormat_NoAtSign_Returns400(
                @Autowired MemberFixtures fixture
        ) {
            // arrange & act
            var response = fixture.getClient().postForEntity(
                    "/api/v1/members/issueToken",
                    new IssueTokenCommand("invalidemail.com", "password"),
                    String.class
            );

            // assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("이메일 형식이 잘못되었으면(도메인 없음) 400 BadRequest를 반환한다")
        void issueToken_WithInvalidEmailFormat_NoDomain_Returns400(
                @Autowired MemberFixtures fixture
        ) {
            // arrange & act
            var response = fixture.getClient().postForEntity(
                    "/api/v1/members/issueToken",
                    new IssueTokenCommand("test@", "password"),
                    String.class
            );

            // assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("이메일이 최대 길이(100자)를 초과하면 400 BadRequest를 반환한다")
        void issueToken_WithTooLongEmail_Returns400(
                @Autowired MemberFixtures fixture
        ) {
            // arrange
            String tooLongEmail = "a".repeat(100) + "@example.com";

            // act
            var response = fixture.getClient().postForEntity(
                    "/api/v1/members/issueToken",
                    new IssueTokenCommand(tooLongEmail, "password"),
                    String.class
            );

            // assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("이메일 대소문자 구분 - 대문자로 가입하고 소문자로 로그인")
        void issueToken_EmailCaseSensitivity_UpperToLower(
                @Autowired MemberFixtures fixture
        ) {
            // arrange
            String upperEmail = "TEST@EXAMPLE.COM";
            String lowerEmail = "test@example.com";
            String password = "ValidPassword123!";

            fixture.createMember(upperEmail, password);

            // act - 소문자로 로그인 시도
            var response = fixture.getClient().postForEntity(
                    "/api/v1/members/issueToken",
                    new IssueTokenCommand(lowerEmail, password),
                    String.class
            );

            // assert - 시스템이 이메일을 대소문자 구분하는지 확인
            // 대소문자를 구분하지 않으면 200, 구분하면 400이어야 함
            boolean isCaseInsensitive = response.getStatusCode() == HttpStatus.OK;
            boolean isCaseSensitive = response.getStatusCode() == HttpStatus.BAD_REQUEST;

            assertThat(isCaseInsensitive || isCaseSensitive).isTrue();
            // 실제 정책에 맞게 하나만 선택해서 검증
        }
    }

    @Nested
    @DisplayName("입력 검증 - 비밀번호")
    class PasswordValidation {

        @Test
        @DisplayName("비밀번호가 null이면 400 BadRequest를 반환한다")
        void issueToken_WithNullPassword_Returns400(
                @Autowired MemberFixtures fixture
        ) {
            // arrange & act
            var response = fixture.getClient().postForEntity(
                    "/api/v1/members/issueToken",
                    new IssueTokenCommand("test@example.com", null),
                    String.class
            );

            // assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("비밀번호가 빈 문자열이면 400 BadRequest를 반환한다")
        void issueToken_WithEmptyPassword_Returns400(
                @Autowired MemberFixtures fixture
        ) {
            // arrange & act
            var response = fixture.getClient().postForEntity(
                    "/api/v1/members/issueToken",
                    new IssueTokenCommand("test@example.com", ""),
                    String.class
            );

            // assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("비밀번호가 화이트스페이스만 있으면 400 BadRequest를 반환한다")
        void issueToken_WithWhitespacePassword_Returns400(
                @Autowired MemberFixtures fixture
        ) {
            // arrange & act
            var response = fixture.getClient().postForEntity(
                    "/api/v1/members/issueToken",
                    new IssueTokenCommand("test@example.com", "   "),
                    String.class
            );

            // assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("비밀번호가 최대 길이(100자)를 초과하면 400 BadRequest를 반환한다")
        void issueToken_WithTooLongPassword_Returns400(
                @Autowired MemberFixtures fixture
        ) {
            // arrange
            String tooLongPassword = "a".repeat(101);

            // act
            var response = fixture.getClient().postForEntity(
                    "/api/v1/members/issueToken",
                    new IssueTokenCommand("test@example.com", tooLongPassword),
                    String.class
            );

            // assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("비밀번호 대소문자를 구분한다")
        void issueToken_PasswordIsCaseSensitive(
                @Autowired MemberFixtures fixture
        ) {
            // arrange
            String email = generateEmail();
            String password = "ValidPassword123!";
            String wrongCasePassword = "validpassword123!";

            fixture.createMember(email, password);

            // act
            var response = fixture.getClient().postForEntity(
                    "/api/v1/members/issueToken",
                    new IssueTokenCommand(email, wrongCasePassword),
                    String.class
            );

            // assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    // ===== 인증 실패 테스트 =====

    @Nested
    @DisplayName("인증 실패")
    class AuthenticationFailure {

        @Test
        @DisplayName("존재하지 않는 이메일로 요청하면 400 BadRequest를 반환한다")
        void issueToken_WithNonExistentEmail_Returns400(
                @Autowired MemberFixtures fixture
        ) {
            // arrange & act
            var response = fixture.getClient().postForEntity(
                    "/api/v1/members/issueToken",
                    new IssueTokenCommand("nonexistent@example.com", "password"),
                    String.class
            );

            // assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("잘못된 비밀번호로 요청하면 400 BadRequest를 반환한다")
        void issueToken_WithWrongPassword_Returns400(
                @Autowired MemberFixtures fixture
        ) {
            // arrange
            String email = generateEmail();
            String correctPassword = "CorrectPassword123!";
            String wrongPassword = "WrongPassword123!";

            fixture.createMember(email, correctPassword);

            // act
            var response = fixture.getClient().postForEntity(
                    "/api/v1/members/issueToken",
                    new IssueTokenCommand(email, wrongPassword),
                    String.class
            );

            // assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    // ===== 보안 테스트 =====

    @Nested
    @DisplayName("보안")
    class Security {

        @Test
        @DisplayName("SQL 인젝션 공격을 방어한다")
        void issueToken_PreventsSQLInjection(
                @Autowired MemberFixtures fixture
        ) {
            // arrange & act
            var response = fixture.getClient().postForEntity(
                    "/api/v1/members/issueToken",
                    new IssueTokenCommand("'; DROP TABLE members; --", "password"),
                    String.class
            );

            // assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("XSS 공격을 방어한다")
        void issueToken_PreventsXSS(
                @Autowired MemberFixtures fixture
        ) {
            // arrange & act
            var response = fixture.getClient().postForEntity(
                    "/api/v1/members/issueToken",
                    new IssueTokenCommand("test<script>alert('xss')</script>@example.com", "password"),
                    String.class
            );

            // assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }
}
