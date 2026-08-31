package com.vbox.disclosure.domain;

import java.time.Instant;

public record Disclosure(Long id, String referenceNumber, String customerId,
                         String description, String status, Instant createdAt) {
}
