package com.vbox.disclosure.messaging;

import java.time.Instant;

/** Event schema shared by Kafka producers and consumers. */
public record ReceiptDisplayEvent(
        String eventId,
        String receiptId,
        String workActionId,
        String customerId,
        String referenceNumber,
        String displayedBy,
        Instant displayedAt) {
}
