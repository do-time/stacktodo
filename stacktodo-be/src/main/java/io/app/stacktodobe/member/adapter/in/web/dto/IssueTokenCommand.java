package io.app.stacktodobe.member.adapter.in.web.dto;

public record IssueTokenCommand(
        String email,
        String password
) {
}
