package com.vbox.disclosure.api;

import com.vbox.disclosure.application.CreateDisclosureReceiptUseCase;
import com.vbox.disclosure.application.DisclosureReceiptSearchResult;
import com.vbox.disclosure.application.DisclosureReceiptSearchUseCase;
import com.vbox.disclosure.domain.DeliveryChannel;
import com.vbox.disclosure.domain.DisclosureReceipt;
import com.vbox.disclosure.domain.DisclosureReceiptStatus;
import com.vbox.disclosure.domain.ReceiptType;
import com.vbox.disclosure.i18n.MessageResolver;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DisclosureReceiptController.class)
class DisclosureReceiptSearchControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateDisclosureReceiptUseCase createUseCase;

    @MockitoBean
    private DisclosureReceiptSearchUseCase searchUseCase;

    @MockitoBean
    private MessageResolver messageResolver;

    @Test
    void shouldSearchDisclosureReceiptsSuccessfully() throws Exception {
        DisclosureReceipt receipt = new DisclosureReceipt("DR10001", "WA10001", "12345", ReceiptType.DISCLOSURE,
                DeliveryChannel.EMAIL, "customer@example.com", Instant.parse("2026-09-03T10:30:00Z"),
                "DISC-REF-10001", "notes", DisclosureReceiptStatus.CREATED, Instant.now(), Instant.now());
        when(searchUseCase.search(any())).thenReturn(new DisclosureReceiptSearchResult(List.of(receipt)));

        mockMvc.perform(post("/api/disclosures/v1/disclosure-receipts/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\":\"12345\",\"status\":\"CREATED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("Disclosure receipts retrieved successfully."))
                .andExpect(jsonPath("$.data.disclosureReceipts[0].disclosureReceiptId").value("DR10001"));
        verify(searchUseCase).search(any());
    }

    @Test
    void shouldReturnEmptyCollectionWhenNoReceiptsMatch() throws Exception {
        when(searchUseCase.search(any())).thenReturn(new DisclosureReceiptSearchResult(List.of()));

        mockMvc.perform(post("/api/disclosures/v1/disclosure-receipts/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"workActionId\":\"WA10001\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("No disclosure receipts found."))
                .andExpect(jsonPath("$.data.disclosureReceipts").isEmpty());
    }

    @Test
    void shouldRejectEmptySearchCriteria() throws Exception {
        mockMvc.perform(post("/api/disclosures/v1/disclosure-receipts/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(searchUseCase);
    }

    @Test
    void shouldRejectInvalidDateRange() throws Exception {
        mockMvc.perform(post("/api/disclosures/v1/disclosure-receipts/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fromReceivedAt\":\"2026-09-09T00:00:00Z\",\"toReceivedAt\":\"2026-09-08T00:00:00Z\"}"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(searchUseCase);
    }
}
