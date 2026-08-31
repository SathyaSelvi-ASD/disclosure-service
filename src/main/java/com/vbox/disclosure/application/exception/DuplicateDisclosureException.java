package com.vbox.disclosure.application.exception;

public class DuplicateDisclosureException extends RuntimeException {
    private final String errorCode = "ERR-DISCLOSURE-DUPLICATE";
    public DuplicateDisclosureException(String referenceNumber) { super("Duplicate disclosure reference: " + referenceNumber); }
    public String getErrorCode() { return errorCode; }
}
