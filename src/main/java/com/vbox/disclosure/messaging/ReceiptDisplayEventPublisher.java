package com.vbox.disclosure.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReceiptDisplayEventPublisher {
    private final KafkaTemplate<String, ReceiptDisplayEvent> kafkaTemplate;

    @Value("${app.kafka.topics.receipt-display}")
    private String receiptDisplayTopic;

    public void publish(ReceiptDisplayEvent event) {
        kafkaTemplate.send(receiptDisplayTopic, event.workActionId(), event);
    }

    public String topic() {
        return receiptDisplayTopic;
    }
}
