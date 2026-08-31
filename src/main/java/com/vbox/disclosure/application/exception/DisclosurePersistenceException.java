package com.vbox.disclosure.application.exception;

public class DisclosurePersistenceException extends RuntimeException {
    private final String errorCode;
    public DisclosurePersistenceException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "ERR-DISCLOSURE-DB-SAVE-FAILED";
    }
    public String getErrorCode() { return errorCode; }
}
