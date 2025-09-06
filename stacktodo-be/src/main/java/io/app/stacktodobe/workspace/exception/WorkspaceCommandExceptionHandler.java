package io.app.stacktodobe.workspace.exception;

import io.app.stacktodobe.member.exception.InvalidCommandException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class WorkspaceCommandExceptionHandler {

    @ExceptionHandler(InvalidCommandException.class)
    public ResponseEntity<Void> handleInvalidCommandException(InvalidCommandException e) {

        return ResponseEntity
                .badRequest()
                .build();
    }
}
