package com.vbox.disclosure.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateDisclosureDto(
        @NotBlank(message = "{disclosure.validation.mandatory-field}")
        @Size(max = 50, message = "{disclosure.validation.reference-number-size}")
        String referenceNumber,
        @NotBlank(message = "{disclosure.validation.mandatory-field}") String customerId,
        @NotBlank(message = "{disclosure.validation.mandatory-field}") String description) {
}
