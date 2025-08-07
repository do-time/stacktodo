package io.app.stacktodobe.member.presentation.controller;

import io.app.stacktodobe.member.presentation.command.MemberCreateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberCommandController {

    @PostMapping("/signup")
    public void signUp(@RequestBody MemberCreateCommand command) {
        // 회원가입 로직을 여기에 구현합니다.
        // 예: MemberCreateCommand를 사용하여 회원 정보를 저장하는 서비스 호출
        // 예: MemberCommandUseCase를 사용하여 비즈니스 로직 처리
        // 예: 성공적으로 회원가입이 완료되면 201 Created 응답 반환
        // 예: 실패 시 적절한 에러 응답 반환
        // 회원 가입이 성공하면 토큰값 반환
        return;
    }
}
