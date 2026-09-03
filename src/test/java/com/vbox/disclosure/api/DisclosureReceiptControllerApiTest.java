package com.vbox.disclosure.api;

import com.vbox.disclosure.application.CreateDisclosureReceiptResult;
import com.vbox.disclosure.application.CreateDisclosureReceiptUseCase;
import com.vbox.disclosure.domain.DeliveryChannel;
import com.vbox.disclosure.domain.DisclosureReceiptStatus;
import com.vbox.disclosure.domain.ReceiptType;
import com.vbox.disclosure.i18n.MessageResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DisclosureReceiptController.class)
class DisclosureReceiptControllerApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateDisclosureReceiptUseCase useCase;

    @MockitoBean
    private MessageResolver messageResolver;

    @BeforeEach
    void setUp() {
        when(messageResolver.get(any(), any(), any())).thenReturn("Validation failed");
    }

    @Test
    void shouldCreateDisclosureReceiptSuccessfully() throws Exception {
        String jsonBody = """
                {
                  "workActionId": "WA-100",
                  "customerId": "CUST-100",
                  "receiptType": "DISCLOSURE",
                  "deliveryChannel": "EMAIL",
                  "recipient": "customer@example.com",
                  "receivedAt": "2026-08-28T00:00:00Z",
                  "referenceNumber": "REF-100",
                  "notes": "Acknowledged"
                }
                """;

        when(useCase.create(any())).thenReturn(new CreateDisclosureReceiptResult(
                "DR-123",
                "WA-100",
                "CUST-100",
                DisclosureReceiptStatus.CREATED
        ));

        mockMvc.perform(post("/api/disclosures/v1/disclosure-receipts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.data.disclosureReceiptId").value("DR-123"))
                .andExpect(jsonPath("$.data.workActionId").value("WA-100"))
                .andExpect(jsonPath("$.data.customerId").value("CUST-100"))
                .andExpect(jsonPath("$.data.status").value("CREATED"));

        verify(useCase).create(any());
    }

    @Test
    void shouldRejectWhenWorkActionIdIsMissing() throws Exception {
        String jsonBody = """
                {
                  "customerId": "CUST-100",
                  "receiptType": "DISCLOSURE",
                  "deliveryChannel": "EMAIL",
                  "recipient": "[REDACTED_EMAIL_ADDRESS_1]",
                  "receivedAt": "2026-08-28T00:00:00Z",
                  "referenceNumber": "REF-100"
                }
                """;

        mockMvc.perform(post("/api/disclosures/v1/disclosure-receipts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors[0].code").value("ERR-VALIDATION-WORKACTIONID"))
                .andExpect(jsonPath("$.errors[0].message").value("workActionId is mandatory"));

        verifyNoInteractions(useCase);
    }
}
