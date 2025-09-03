package io.app.stacktodobe.member.adapter.in.web.controller;


import io.app.stacktodobe.member.adapter.in.web.dto.MemberView;
import io.app.stacktodobe.member.application.port.in.usecase.MemberQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberQueryController {

    private final MemberQueryUseCase memberQueryUseCase;

    @GetMapping("/me")
    ResponseEntity<MemberView> memberMe(Principal principal) {
        UUID id = UUID.fromString(principal.getName());
        System.out.println("Member ID from Principal: " + id); // Debugging line
        MemberView memberView = memberQueryUseCase.memberMe(id);

        return ResponseEntity.ok(memberView);
    }
}
