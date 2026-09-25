package com.vbox.disclosure.application.exception;

public class WorkActionNotFoundException extends RuntimeException {
    private final String workActionId;

    public WorkActionNotFoundException(String workActionId) {
        super("Work action not found: " + workActionId);
        this.workActionId = workActionId;
    }

    public String getWorkActionId() {
        return workActionId;
    }
}
