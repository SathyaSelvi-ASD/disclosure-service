package com.vbox.disclosure.application;

import com.vbox.disclosure.api.dto.request.CreateDisclosureReceiptRequest;
import com.vbox.disclosure.domain.DeliveryChannel;
import com.vbox.disclosure.domain.ReceiptType;

import java.time.Instant;

public record CreateDisclosureReceiptCommand(
        String workActionId,
        String customerId,
        ReceiptType receiptType,
        DeliveryChannel deliveryChannel,
        String recipient,
        Instant receivedAt,
        String referenceNumber,
        String notes
) {
    public static CreateDisclosureReceiptCommand fromRequest(CreateDisclosureReceiptRequest request) {
        return new CreateDisclosureReceiptCommand(
                request.workActionId(),
                request.customerId(),
                request.receiptType(),
                request.deliveryChannel(),
                request.recipient(),
                request.receivedAt(),
                request.referenceNumber(),
                request.notes()
        );
    }
}
