package com.vbox.disclosure.api.dto.response;

import com.vbox.disclosure.domain.DisclosureReceiptStatus;

public record CreateDisclosureReceiptResponse(
        String disclosureReceiptId,
        String workActionId,
        String customerId,
        DisclosureReceiptStatus status
) {
}
