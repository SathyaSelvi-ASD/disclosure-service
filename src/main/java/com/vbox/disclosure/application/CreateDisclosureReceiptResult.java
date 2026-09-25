package com.vbox.disclosure.application;

import com.vbox.disclosure.domain.DisclosureReceiptStatus;

public record CreateDisclosureReceiptResult(
        String disclosureReceiptId,
        String workActionId,
        String customerId,
        DisclosureReceiptStatus status
) {
}
