package com.vbox.disclosure.application;

import com.vbox.disclosure.application.exception.DuplicateDisclosureReceiptException;
import com.vbox.disclosure.application.exception.InvalidDisclosureReceiptException;
import com.vbox.disclosure.application.exception.WorkActionNotFoundException;
import com.vbox.disclosure.domain.DeliveryChannel;
import com.vbox.disclosure.domain.DisclosureReceipt;
import com.vbox.disclosure.domain.DisclosureReceiptRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class CreateDisclosureReceiptUseCase {
    private final DisclosureReceiptRepository repository;
    private final WorkActionGateway workActionGateway;

    public CreateDisclosureReceiptUseCase(DisclosureReceiptRepository repository, WorkActionGateway workActionGateway) {
        this.repository = repository;
        this.workActionGateway = workActionGateway;
    }

    @Transactional
    public CreateDisclosureReceiptResult create(CreateDisclosureReceiptCommand command) {
        validateMandatoryFields(command);

        if (!workActionGateway.exists(command.workActionId())) {
            throw new WorkActionNotFoundException(command.workActionId());
        }

        if (repository.existsByWorkActionIdAndReceiptTypeAndReferenceNumber(
                command.workActionId(),
                command.receiptType(),
                command.referenceNumber())) {
            throw new DuplicateDisclosureReceiptException(command.workActionId(), command.receiptType(), command.referenceNumber());
        }

        if (command.deliveryChannel() == DeliveryChannel.EMAIL && !isValidEmail(command.recipient())) {
            throw new InvalidDisclosureReceiptException("EMAIL delivery requires valid email recipient");
        }

        if (command.receivedAt() == null || command.receivedAt().isAfter(Instant.now())) {
            throw new InvalidDisclosureReceiptException("receivedAt cannot be future date");
        }

        DisclosureReceipt saved = repository.save(DisclosureReceipt.create(
                command.workActionId(),
                command.customerId(),
                command.receiptType(),
                command.deliveryChannel(),
                command.recipient(),
                command.receivedAt(),
                command.referenceNumber(),
                command.notes()
        ));

        return new CreateDisclosureReceiptResult(
                saved.disclosureReceiptId(),
                saved.workActionId(),
                saved.customerId(),
                saved.status()
        );
    }

    private void validateMandatoryFields(CreateDisclosureReceiptCommand command) {
        if (command == null) {
            throw new InvalidDisclosureReceiptException("Request payload is required");
        }
        if (command.workActionId() == null || command.workActionId().isBlank()) {
            throw new InvalidDisclosureReceiptException("workActionId is mandatory");
        }
        if (command.customerId() == null || command.customerId().isBlank()) {
            throw new InvalidDisclosureReceiptException("customerId is mandatory");
        }
        if (command.receiptType() == null) {
            throw new InvalidDisclosureReceiptException("receiptType is mandatory");
        }
        if (command.deliveryChannel() == null) {
            throw new InvalidDisclosureReceiptException("deliveryChannel is mandatory");
        }
        if (command.recipient() == null || command.recipient().isBlank()) {
            throw new InvalidDisclosureReceiptException("recipient is mandatory");
        }
        if (command.receivedAt() == null) {
            throw new InvalidDisclosureReceiptException("receivedAt is mandatory");
        }
        if (command.referenceNumber() == null || command.referenceNumber().isBlank()) {
            throw new InvalidDisclosureReceiptException("referenceNumber is mandatory");
        }
    }

    private boolean isValidEmail(String recipient) {
        return recipient != null && recipient.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}
