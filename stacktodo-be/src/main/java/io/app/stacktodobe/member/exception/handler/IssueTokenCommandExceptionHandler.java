package io.app.stacktodobe.member.exception.handler;

import io.app.stacktodobe.member.exception.InvalidCommandException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class IssueTokenCommandExceptionHandler {

    @ExceptionHandler(InvalidCommandException.class)
    ResponseEntity<?> handleInvalidCommandException(InvalidCommandException e) {
        return ResponseEntity
                .badRequest()
                .build();

    }
}
