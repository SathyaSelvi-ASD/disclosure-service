package com.vbox.disclosure.i18n;

public enum MessageKey {
    GREETING("disclosure.greeting"),
    CREATE_SUCCESS("disclosure.create.success"),
    VALIDATION_FAILED("disclosure.validation.failed"),
    DUPLICATE("disclosure.error.duplicate"),
    PERSISTENCE_ERROR("disclosure.error.persistence"),
    UNEXPECTED_ERROR("disclosure.error.unexpected");
    private final String key;
    MessageKey(String key) { this.key = key; }
    public String key() { return key; }
}
