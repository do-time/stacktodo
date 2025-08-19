package io.app.stacktodobe.member.presentation.command.controller;

import io.app.stacktodobe.member.domain.usecase.IssueTokenCommandUseCase;
import io.app.stacktodobe.member.presentation.command.AccessTokenCarrier;
import io.app.stacktodobe.member.presentation.command.IssueTokenCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class IssueTokenCommandController {

    private final IssueTokenCommandUseCase issueTokenCommandUseCase;

    @PostMapping("/issueToken")
    ResponseEntity<AccessTokenCarrier> issueToken(@RequestBody IssueTokenCommand command) {
        return ResponseEntity.ok(
                issueTokenCommandUseCase.issueToken(command));
    }
}
