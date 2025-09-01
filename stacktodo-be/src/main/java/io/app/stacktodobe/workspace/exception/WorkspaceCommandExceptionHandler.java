package io.app.stacktodobe.workspace.exception;

import io.app.stacktodobe.member.adapter.model.InvalidCommandException;
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
