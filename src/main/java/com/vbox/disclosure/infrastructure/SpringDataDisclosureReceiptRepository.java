package com.vbox.disclosure.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataDisclosureReceiptRepository extends JpaRepository<DisclosureReceiptJpaEntity, Long> {
    boolean existsByWorkActionIdAndReceiptTypeAndReferenceNumber(String workActionId, com.vbox.disclosure.domain.ReceiptType receiptType, String referenceNumber);
}
