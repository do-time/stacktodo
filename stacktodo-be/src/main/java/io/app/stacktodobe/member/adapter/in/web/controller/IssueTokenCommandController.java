package io.app.stacktodobe.member.adapter.in.web.controller;

import io.app.stacktodobe.member.adapter.in.web.dto.AccessTokenCarrier;
import io.app.stacktodobe.member.application.port.in.command.IssueTokenCommand;
import io.app.stacktodobe.member.application.usecase.IssueTokenCommandUseCase;
import jakarta.validation.Valid;
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
    ResponseEntity<AccessTokenCarrier> issueToken(@Valid @RequestBody IssueTokenCommand command) {
        System.out.println("Received IssueTokenCommand: " + command); // Debugging line
        return ResponseEntity.ok(
                issueTokenCommandUseCase.issueToken(command));
    }
}
