package com.vbox.disclosure.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReceiptDisplayRequest(
        @NotBlank @Size(max = 100) String receiptId,
        @NotBlank @Size(max = 100) String workActionId,
        @NotBlank @Size(max = 100) String customerId,
        @NotBlank @Size(max = 100) String referenceNumber,
        @NotBlank @Size(max = 100) String displayedBy) {
}
