package io.app.stacktodobe.member.presentation.command;

public record IssueTokenCommand(
        String email,
        String password
) {
}
