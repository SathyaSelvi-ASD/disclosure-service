package com.vbox.disclosure.application;

import com.vbox.disclosure.api.dto.request.ReceiptDisplayRequest;
import com.vbox.disclosure.api.dto.response.ReceiptDisplayResponse;
import com.vbox.disclosure.messaging.ReceiptDisplayEvent;
import com.vbox.disclosure.messaging.ReceiptDisplayEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReceiptDisplayUseCase {
    private final ReceiptDisplayEventPublisher publisher;

    public ReceiptDisplayResponse display(ReceiptDisplayRequest request) {
        Instant displayedAt = Instant.now();
        String eventId = UUID.randomUUID().toString();
        publisher.publish(new ReceiptDisplayEvent(eventId, request.receiptId(), request.workActionId(),
                request.customerId(), request.referenceNumber(), request.displayedBy(), displayedAt));
        return new ReceiptDisplayResponse(eventId, request.receiptId(), publisher.topic(), displayedAt);
    }
}
