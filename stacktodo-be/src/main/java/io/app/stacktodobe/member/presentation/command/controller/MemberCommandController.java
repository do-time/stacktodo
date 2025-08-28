package io.app.stacktodobe.member.presentation.command.controller;

import io.app.stacktodobe.member.domain.usecase.MemberCommandUseCase;
import io.app.stacktodobe.member.presentation.command.MemberCreateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberCommandController {
    private final MemberCommandUseCase memberCommandUseCase;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody MemberCreateCommand command) {
        memberCommandUseCase.createMember(command, passwordEncoder.encode(command.password()));
        // 회원 가입 후에는 일반적으로 201 Created 상태 코드를 반환하는 것이 좋지만,
        // 여기서는 요청이 성공적으로 처리되었음을 나타내기 위해 204 No Content를 반환합니다.
        // 이는 클라이언트가 추가적인 작업을 하지 않아도 된다는 것을 의미합니다.
        // 만약 클라이언트가 회원 가입 후에 추가적인 정보를 필요로 한다면,
        // 201 Created와 함께 생성된 리소스의 URI를 반환하는 것이 더 적절할 수 있습니다.

        return ResponseEntity.noContent().build();
    }
}
