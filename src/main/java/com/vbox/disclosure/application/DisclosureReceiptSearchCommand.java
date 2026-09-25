package com.vbox.disclosure.application;

import com.vbox.disclosure.api.dto.request.DisclosureReceiptSearchRequest;
import com.vbox.disclosure.domain.DeliveryChannel;
import com.vbox.disclosure.domain.DisclosureReceiptStatus;
import com.vbox.disclosure.domain.ReceiptType;

import java.time.Instant;

public record DisclosureReceiptSearchCommand(
        String workActionId, String customerId, ReceiptType receiptType, DeliveryChannel deliveryChannel,
        DisclosureReceiptStatus status, String referenceNumber, Instant fromReceivedAt, Instant toReceivedAt
) {
    public static DisclosureReceiptSearchCommand fromRequest(DisclosureReceiptSearchRequest request) {
        return new DisclosureReceiptSearchCommand(request.workActionId(), request.customerId(), request.receiptType(),
                request.deliveryChannel(), request.status(), request.referenceNumber(), request.fromReceivedAt(), request.toReceivedAt());
    }
}
