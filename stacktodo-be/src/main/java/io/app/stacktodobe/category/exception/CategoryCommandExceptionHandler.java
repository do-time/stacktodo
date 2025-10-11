package io.app.stacktodobe.category.exception;

import io.app.stacktodobe.member.exception.InvalidCommandException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class CategoryCommandExceptionHandler {
    @ExceptionHandler(InvalidCommandException.class)
    public ResponseEntity<?> handleInvalidCommandException(InvalidCommandException e) {

        return ResponseEntity
                .badRequest()
                .body("Invalid command: " + e.getMessage());
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<?> handleInvalidCommandException(CategoryNotFoundException e) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
}
