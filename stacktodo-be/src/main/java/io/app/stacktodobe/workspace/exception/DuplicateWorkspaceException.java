package io.app.stacktodobe.workspace.exception;

public class DuplicateWorkspaceException extends RuntimeException {
    public DuplicateWorkspaceException(String message) {
        super(message);
    }

    public DuplicateWorkspaceException(String message, Throwable cause) {
        super(message, cause);
    }
}
