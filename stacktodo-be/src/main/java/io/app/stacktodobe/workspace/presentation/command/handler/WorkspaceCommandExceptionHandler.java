package io.app.stacktodobe.workspace.presentation.command.handler;

import io.app.stacktodobe.member.exception.InvalidCommandException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class WorkspaceCommandExceptionHandler {
    @ExceptionHandler(InvalidCommandException.class)
    public ResponseEntity<?> handleInvalidCommandException(InvalidCommandException e) {

        return ResponseEntity
                .badRequest()
                .body("Invalid command: " + e.getMessage());

    }
}
