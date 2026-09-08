package com.vbox.disclosure.api.dto.response;

import com.vbox.disclosure.domain.DeliveryChannel;
import com.vbox.disclosure.domain.DisclosureReceiptStatus;
import com.vbox.disclosure.domain.ReceiptType;

import java.time.Instant;

public record DisclosureReceiptResponse(
        String disclosureReceiptId,
        String workActionId,
        String customerId,
        ReceiptType receiptType,
        DeliveryChannel deliveryChannel,
        String recipient,
        Instant receivedAt,
        String referenceNumber,
        DisclosureReceiptStatus status
) {
}
