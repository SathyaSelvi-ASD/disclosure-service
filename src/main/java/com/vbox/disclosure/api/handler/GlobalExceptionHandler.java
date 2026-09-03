package com.vbox.disclosure.api.handler;

import com.vbox.disclosure.api.dto.response.ApiMessage;
import com.vbox.disclosure.api.dto.response.ApiResponse;
import com.vbox.disclosure.application.exception.*;
import com.vbox.disclosure.i18n.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final MessageResolver messages;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.info("Handling request validation failure fieldErrorCount={}", ex.getBindingResult().getFieldErrorCount());
        List<ApiMessage> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> new ApiMessage("ERR-VALIDATION-" + e.getField().toUpperCase(), e.getDefaultMessage()))
                .toList();
        return build(HttpStatus.BAD_REQUEST, messages.get(MessageKey.VALIDATION_FAILED, LocaleContextHolder.getLocale()), errors);
    }

    @ExceptionHandler(DuplicateDisclosureException.class)
    public ResponseEntity<ApiResponse> handleDuplicate(DuplicateDisclosureException ex) {
        log.info("Handling duplicate disclosure error code={}", ex.getErrorCode());
        String message = messages.get(MessageKey.DUPLICATE, LocaleContextHolder.getLocale());
        return build(HttpStatus.CONFLICT, message, List.of(new ApiMessage(ex.getErrorCode(), message)));
    }

    @ExceptionHandler(DisclosurePersistenceException.class)
    public ResponseEntity<ApiResponse> handlePersistence(DisclosurePersistenceException ex) {
        log.error("Handling disclosure persistence error code={}", ex.getErrorCode(), ex);
        String message = messages.get(MessageKey.PERSISTENCE_ERROR, LocaleContextHolder.getLocale());
        return build(HttpStatus.INTERNAL_SERVER_ERROR, message, List.of(new ApiMessage(ex.getErrorCode(), message)));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleUnexpected(Exception ex) {
        log.error("Handling unexpected error", ex);
        String message = messages.get(MessageKey.UNEXPECTED_ERROR, LocaleContextHolder.getLocale());
        return build(HttpStatus.INTERNAL_SERVER_ERROR, message, List.of(new ApiMessage("ERR-INTERNAL", message)));
    }

    private ResponseEntity<ApiResponse> build(HttpStatus status, String message, List<ApiMessage> errors) {
        var body = new ApiResponse("ERROR", status.value(), message, errors, List.of(), Map.of());
        return ResponseEntity.status(status).body(body);
    }
}
