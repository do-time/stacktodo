package io.app.stacktodobe.member.presentation.query.controller;

import io.app.stacktodobe.member.domain.usecase.MemberQueryUseCase;
import io.app.stacktodobe.member.presentation.view.MemberMeView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberMeController {

    private final MemberQueryUseCase memberQueryUseCase;



    @GetMapping("/me")
    MemberMeView getMemberInfo(
            Principal user
    ) {
        System.out.println("user :" +user.getName());
        UUID id = UUID.fromString(user.getName());
        return memberQueryUseCase.getMemberMe(id);
    }
}
