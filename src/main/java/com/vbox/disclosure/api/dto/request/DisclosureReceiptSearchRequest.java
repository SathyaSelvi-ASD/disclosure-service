package com.vbox.disclosure.api.dto.request;

import com.vbox.disclosure.domain.DeliveryChannel;
import com.vbox.disclosure.domain.DisclosureReceiptStatus;
import com.vbox.disclosure.domain.ReceiptType;
import jakarta.validation.constraints.AssertTrue;

import java.time.Instant;

public record DisclosureReceiptSearchRequest(
        String workActionId,
        String customerId,
        ReceiptType receiptType,
        DeliveryChannel deliveryChannel,
        DisclosureReceiptStatus status,
        String referenceNumber,
        Instant fromReceivedAt,
        Instant toReceivedAt
) {
    @AssertTrue(message = "At least one search criterion must be provided")
    public boolean isSearchCriteriaProvided() {
        return hasText(workActionId) || hasText(customerId) || receiptType != null || deliveryChannel != null
                || status != null || hasText(referenceNumber) || fromReceivedAt != null || toReceivedAt != null;
    }

    @AssertTrue(message = "fromReceivedAt cannot be after toReceivedAt")
    public boolean isReceivedAtRangeValid() {
        return fromReceivedAt == null || toReceivedAt == null || !fromReceivedAt.isAfter(toReceivedAt);
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
