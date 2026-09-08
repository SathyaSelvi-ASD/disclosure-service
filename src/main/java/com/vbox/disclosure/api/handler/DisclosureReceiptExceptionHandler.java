package com.vbox.disclosure.api.handler;

import com.vbox.disclosure.api.dto.response.ApiMessage;
import com.vbox.disclosure.api.dto.response.ApiResponse;
import com.vbox.disclosure.application.exception.DuplicateDisclosureReceiptException;
import com.vbox.disclosure.application.exception.InvalidDisclosureReceiptException;
import com.vbox.disclosure.application.exception.WorkActionNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.List;
import java.util.Comparator;

@RestControllerAdvice
public class DisclosureReceiptExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<ApiMessage> errors = ex.getBindingResult().getFieldErrors().stream()
                .sorted(Comparator
                        .comparing((org.springframework.validation.FieldError fieldError) -> isDerivedValidationField(fieldError.getField()))
                        .thenComparing(org.springframework.validation.FieldError::getField))
                .map(fieldError -> new ApiMessage(
                        "ERR-VALIDATION-" + fieldError.getField().toUpperCase(),
                        fieldError.getDefaultMessage()))
                .toList();

        return build(HttpStatus.BAD_REQUEST, "Validation failed", errors, null);
    }

    @ExceptionHandler(WorkActionNotFoundException.class)
    public ResponseEntity<ApiResponse> handleWorkActionNotFound(WorkActionNotFoundException ex) {
        String message = "Work action " + ex.getWorkActionId() + " does not exist.";
        return build(
                HttpStatus.NOT_FOUND,
                message,
                List.of(new ApiMessage("ERR-WORK-ACTION-NOT-FOUND", message))
        );
    }

    @ExceptionHandler(DuplicateDisclosureReceiptException.class)
    public ResponseEntity<ApiResponse> handleDuplicateReceipt(DuplicateDisclosureReceiptException ex) {
        String message = "A disclosure receipt already exists for the provided workActionId, receiptType and referenceNumber.";
        return build(
                HttpStatus.CONFLICT,
                message,
                List.of(new ApiMessage("ERR-DUPLICATE-DISCLOSURE-RECEIPT", message))
        );
    }

    @ExceptionHandler(InvalidDisclosureReceiptException.class)
    public ResponseEntity<ApiResponse> handleInvalidReceipt(InvalidDisclosureReceiptException ex) {
        String message = ex.getMessage();
        return build(
                HttpStatus.BAD_REQUEST,
                message,
                List.of(new ApiMessage("ERR-INVALID-DISCLOSURE-RECEIPT", message))
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> handleUnreadableRequest(HttpMessageNotReadableException ex) {
        String message = "Request contains an invalid value or date/time format.";
        return build(HttpStatus.BAD_REQUEST, message,
                List.of(new ApiMessage("ERR-VALIDATION-REQUEST", message)));
    }

    private ResponseEntity<ApiResponse> build(HttpStatus status, String message, List<ApiMessage> errors) {
        return build(status, message, errors, java.util.Map.of());
    }

    private boolean isDerivedValidationField(String field) {
        return field.endsWith("ValidForDeliveryChannel") || field.endsWith("NotInFuture")
                || field.endsWith("CriteriaProvided") || field.endsWith("RangeValid");
    }

    private ResponseEntity<ApiResponse> build(HttpStatus status, String message, List<ApiMessage> errors, Object data) {
        Object responseData = data == null ? java.util.Map.of() : data;
        ApiResponse response = new ApiResponse("ERROR", status.value(), message, errors, List.of(), responseData);
        return ResponseEntity.status(status).body(response);
    }
}
