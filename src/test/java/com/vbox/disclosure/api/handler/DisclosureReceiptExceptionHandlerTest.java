package com.vbox.disclosure.api.handler;

import com.vbox.disclosure.api.dto.response.ApiResponse;
import com.vbox.disclosure.application.exception.DuplicateDisclosureReceiptException;
import com.vbox.disclosure.application.exception.WorkActionNotFoundException;
import com.vbox.disclosure.domain.ReceiptType;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DisclosureReceiptExceptionHandlerTest {

    private final DisclosureReceiptExceptionHandler handler = new DisclosureReceiptExceptionHandler();

    @Test
    void shouldReturnStandardErrorEnvelopeForWorkActionNotFound() {
        ResponseEntity<ApiResponse> response = handler.handleWorkActionNotFound(new WorkActionNotFoundException("WA-404"));

        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("ERROR", response.getBody().status());
        assertEquals(404, response.getBody().statusCode());
        assertEquals("ERR-WORK-ACTION-NOT-FOUND", response.getBody().errors().getFirst().code());
        assertEquals("Work action WA-404 does not exist.", response.getBody().message());
        assertEquals("{}", response.getBody().data().toString());
    }

    @Test
    void shouldReturnStandardErrorEnvelopeForDuplicateReceipt() {
        ResponseEntity<ApiResponse> response = handler.handleDuplicateReceipt(
                new DuplicateDisclosureReceiptException("WA-100", ReceiptType.DISCLOSURE, "REF-100"));

        assertEquals(409, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("ERROR", response.getBody().status());
        assertEquals(409, response.getBody().statusCode());
        assertEquals("ERR-DUPLICATE-DISCLOSURE-RECEIPT", response.getBody().errors().getFirst().code());
        assertEquals("A disclosure receipt already exists for the provided workActionId, receiptType and referenceNumber.", response.getBody().message());
        assertEquals("{}", response.getBody().data().toString());
    }
}
