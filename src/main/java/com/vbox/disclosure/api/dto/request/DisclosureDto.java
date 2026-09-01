package com.vbox.disclosure.api.dto.request;

import java.time.Instant;

public record DisclosureDto(Long id, String referenceNumber, String customerId,
                            String description, String disclosureStatus, Instant createdAt) {
}
