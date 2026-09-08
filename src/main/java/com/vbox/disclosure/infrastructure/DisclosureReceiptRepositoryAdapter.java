package com.vbox.disclosure.infrastructure;

import com.vbox.disclosure.domain.DisclosureReceipt;
import com.vbox.disclosure.domain.DisclosureReceiptRepository;
import com.vbox.disclosure.domain.ReceiptType;
import com.vbox.disclosure.application.DisclosureReceiptSearchCommand;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    @Override
    public List<DisclosureReceipt> search(DisclosureReceiptSearchCommand command) {
        Specification<DisclosureReceiptJpaEntity> specification = Specification.allOf(
                equalsIfSupplied("workActionId", command.workActionId()),
                equalsIfSupplied("customerId", command.customerId()),
                equalsIfSupplied("receiptType", command.receiptType()),
                equalsIfSupplied("deliveryChannel", command.deliveryChannel()),
                equalsIfSupplied("status", command.status()),
                equalsIfSupplied("referenceNumber", command.referenceNumber()),
                fromReceivedAtIfSupplied(command.fromReceivedAt()),
                toReceivedAtIfSupplied(command.toReceivedAt())
        );

        return repository.findAll(specification).stream().map(this::toDomain).toList();
    }

    private Specification<DisclosureReceiptJpaEntity> equalsIfSupplied(String attribute, Object value) {
        return value == null ? null : (root, query, builder) -> builder.equal(root.get(attribute), value);
    }

    private Specification<DisclosureReceiptJpaEntity> fromReceivedAtIfSupplied(java.time.Instant fromReceivedAt) {
        return fromReceivedAt == null ? null : (root, query, builder) ->
                builder.greaterThanOrEqualTo(root.get("receivedAt"), fromReceivedAt);
    }

    private Specification<DisclosureReceiptJpaEntity> toReceivedAtIfSupplied(java.time.Instant toReceivedAt) {
        return toReceivedAt == null ? null : (root, query, builder) ->
                builder.lessThanOrEqualTo(root.get("receivedAt"), toReceivedAt);
    }

    private DisclosureReceipt toDomain(DisclosureReceiptJpaEntity entity) {
        return new DisclosureReceipt(
                entity.getDisclosureReceiptId(), entity.getWorkActionId(), entity.getCustomerId(),
                entity.getReceiptType(), entity.getDeliveryChannel(), entity.getRecipient(), entity.getReceivedAt(),
                entity.getReferenceNumber(), entity.getNotes(), entity.getStatus(), entity.getCreatedAt(), entity.getUpdatedAt());
    }
}
