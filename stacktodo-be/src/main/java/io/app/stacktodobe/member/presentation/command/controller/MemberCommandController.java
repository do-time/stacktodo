package io.app.stacktodobe.member.presentation.command.controller;

import io.app.stacktodobe.member.domain.usecase.MemberCommandUseCase;
import io.app.stacktodobe.member.presentation.command.MemberCreateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberCommandController {
    private final MemberCommandUseCase memberCommandUseCase;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody MemberCreateCommand command) {
        memberCommandUseCase.createMember(command);
        return ResponseEntity.noContent().build();
    }
}
