package com.vbox.disclosure.domain;

public interface DisclosureReceiptRepository {
    DisclosureReceipt save(DisclosureReceipt disclosureReceipt);

    boolean existsByWorkActionIdAndReceiptTypeAndReferenceNumber(String workActionId, ReceiptType receiptType, String referenceNumber);

    java.util.List<DisclosureReceipt> search(com.vbox.disclosure.application.DisclosureReceiptSearchCommand command);
}
