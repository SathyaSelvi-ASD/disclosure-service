package com.vbox.disclosure.application;

import com.vbox.disclosure.application.exception.InvalidDisclosureReceiptException;
import com.vbox.disclosure.domain.DisclosureReceiptRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DisclosureReceiptSearchUseCase {
    private final DisclosureReceiptRepository repository;

    public DisclosureReceiptSearchUseCase(DisclosureReceiptRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public DisclosureReceiptSearchResult search(DisclosureReceiptSearchCommand command) {
        validate(command);
        return new DisclosureReceiptSearchResult(List.copyOf(repository.search(command)));
    }

    private void validate(DisclosureReceiptSearchCommand command) {
        if (command == null) {
            throw new InvalidDisclosureReceiptException("Request payload is required");
        }
        boolean noCriteria = isBlank(command.workActionId()) && isBlank(command.customerId())
                && command.receiptType() == null && command.deliveryChannel() == null && command.status() == null
                && isBlank(command.referenceNumber()) && command.fromReceivedAt() == null && command.toReceivedAt() == null;
        if (noCriteria) {
            throw new InvalidDisclosureReceiptException("At least one search criterion must be provided");
        }
        if (command.fromReceivedAt() != null && command.toReceivedAt() != null
                && command.fromReceivedAt().isAfter(command.toReceivedAt())) {
            throw new InvalidDisclosureReceiptException("fromReceivedAt cannot be after toReceivedAt");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
