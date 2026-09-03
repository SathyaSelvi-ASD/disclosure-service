package com.vbox.disclosure.infrastructure;

import com.vbox.disclosure.domain.DisclosureReceipt;
import com.vbox.disclosure.domain.DisclosureReceiptRepository;
import com.vbox.disclosure.domain.ReceiptType;
import org.springframework.stereotype.Repository;

@Repository
public class DisclosureReceiptRepositoryAdapter implements DisclosureReceiptRepository {
    private final SpringDataDisclosureReceiptRepository repository;

    public DisclosureReceiptRepositoryAdapter(SpringDataDisclosureReceiptRepository repository) {
        this.repository = repository;
    }

    @Override
    public DisclosureReceipt save(DisclosureReceipt disclosureReceipt) {
        DisclosureReceiptJpaEntity entity = new DisclosureReceiptJpaEntity(
                disclosureReceipt.disclosureReceiptId(),
                disclosureReceipt.workActionId(),
                disclosureReceipt.customerId(),
                disclosureReceipt.receiptType(),
                disclosureReceipt.deliveryChannel(),
                disclosureReceipt.recipient(),
                disclosureReceipt.receivedAt(),
                disclosureReceipt.referenceNumber(),
                disclosureReceipt.notes(),
                disclosureReceipt.status(),
                disclosureReceipt.createdAt(),
                disclosureReceipt.updatedAt()
        );
        DisclosureReceiptJpaEntity saved = repository.save(entity);
        return new DisclosureReceipt(
                saved.getDisclosureReceiptId(),
                saved.getWorkActionId(),
                saved.getCustomerId(),
                saved.getReceiptType(),
                saved.getDeliveryChannel(),
                saved.getRecipient(),
                saved.getReceivedAt(),
                saved.getReferenceNumber(),
                saved.getNotes(),
                saved.getStatus(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }

    @Override
    public boolean existsByWorkActionIdAndReceiptTypeAndReferenceNumber(String workActionId, ReceiptType receiptType, String referenceNumber) {
        return repository.existsByWorkActionIdAndReceiptTypeAndReferenceNumber(workActionId, receiptType, referenceNumber);
    }
}
