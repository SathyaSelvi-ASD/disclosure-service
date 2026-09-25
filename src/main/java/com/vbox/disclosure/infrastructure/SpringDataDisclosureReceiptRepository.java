package com.vbox.disclosure.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SpringDataDisclosureReceiptRepository extends JpaRepository<DisclosureReceiptJpaEntity, Long>, JpaSpecificationExecutor<DisclosureReceiptJpaEntity> {
    boolean existsByWorkActionIdAndReceiptTypeAndReferenceNumber(String workActionId, com.vbox.disclosure.domain.ReceiptType receiptType, String referenceNumber);
}
