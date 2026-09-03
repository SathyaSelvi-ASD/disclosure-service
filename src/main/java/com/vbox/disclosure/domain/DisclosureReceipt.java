package com.vbox.disclosure.domain;

import java.time.Instant;
import java.util.UUID;

public record DisclosureReceipt(
        String disclosureReceiptId,
        String workActionId,
        String customerId,
        ReceiptType receiptType,
        DeliveryChannel deliveryChannel,
        String recipient,
        Instant receivedAt,
        String referenceNumber,
        String notes,
        DisclosureReceiptStatus status,
        Instant createdAt,
        Instant updatedAt
) {
    public static DisclosureReceipt create(
            String workActionId,
            String customerId,
            ReceiptType receiptType,
            DeliveryChannel deliveryChannel,
            String recipient,
            Instant receivedAt,
            String referenceNumber,
            String notes
    ) {
        Instant now = Instant.now();
        String id = "DR" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return new DisclosureReceipt(
                id,
                workActionId,
                customerId,
                receiptType,
                deliveryChannel,
                recipient,
                receivedAt,
                referenceNumber,
                notes,
                DisclosureReceiptStatus.CREATED,
                now,
                now
        );
    }
}
