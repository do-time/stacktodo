package io.app.stacktodobe.member.adapter.in.web.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class IndexController {
    @GetMapping("/")
    public String index(@AuthenticationPrincipal Principal principal) {
        System.out.println("Principal: " + principal);
        return "Hello, StacktodoBe!";
    }
}
