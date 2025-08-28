package io.app.stacktodobe.member.presentation.command.handler;


import io.app.stacktodobe.member.adapter.model.InvalidCommandException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MemberCommandExceptionHandler {

    @ExceptionHandler(InvalidCommandException.class)
    public ResponseEntity<Void> handleInvalidCommandException(InvalidCommandException e) {

        return ResponseEntity
                .badRequest()
                .build();
    }
}
