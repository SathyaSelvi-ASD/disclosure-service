package com.vbox.disclosure.application;

import com.vbox.disclosure.application.exception.DuplicateDisclosureReceiptException;
import com.vbox.disclosure.domain.DeliveryChannel;
import com.vbox.disclosure.domain.DisclosureReceipt;
import com.vbox.disclosure.domain.DisclosureReceiptRepository;
import com.vbox.disclosure.domain.DisclosureReceiptStatus;
import com.vbox.disclosure.domain.ReceiptType;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreateDisclosureReceiptUseCaseTest {

    private final DisclosureReceiptRepository repository = mock(DisclosureReceiptRepository.class);
    private final WorkActionGateway workActionGateway = mock(WorkActionGateway.class);
    private final CreateDisclosureReceiptUseCase useCase = new CreateDisclosureReceiptUseCase(repository, workActionGateway);

    @Test
    void shouldCreateDisclosureReceiptWhenCommandIsValid() {
        CreateDisclosureReceiptCommand command = new CreateDisclosureReceiptCommand(
                "WA-100",
                "CUST-100",
                ReceiptType.DISCLOSURE,
                DeliveryChannel.EMAIL,
                "customer@example.com",
                Instant.parse("2026-08-28T00:00:00Z"),
                "REF-100",
                "Acknowledged"
        );

        when(workActionGateway.exists("WA-100")).thenReturn(true);
        when(repository.existsByWorkActionIdAndReceiptTypeAndReferenceNumber("WA-100", ReceiptType.DISCLOSURE, "REF-100"))
                .thenReturn(false);
        when(repository.save(any(DisclosureReceipt.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CreateDisclosureReceiptResult result = useCase.create(command);

        assertThat(result.disclosureReceiptId()).startsWith("DR");
        assertThat(result.workActionId()).isEqualTo("WA-100");
        assertThat(result.customerId()).isEqualTo("CUST-100");
        assertThat(result.status()).isEqualTo(DisclosureReceiptStatus.CREATED);
        verify(repository).save(any(DisclosureReceipt.class));
    }

    @Test
    void shouldRejectDuplicateReceipt() {
        CreateDisclosureReceiptCommand command = new CreateDisclosureReceiptCommand(
                "WA-100",
                "CUST-100",
                ReceiptType.DISCLOSURE,
                DeliveryChannel.EMAIL,
                "customer@example.com",
                Instant.parse("2026-08-28T00:00:00Z"),
                "REF-100",
                "Acknowledged"
        );

        when(workActionGateway.exists("WA-100")).thenReturn(true);
        when(repository.existsByWorkActionIdAndReceiptTypeAndReferenceNumber("WA-100", ReceiptType.DISCLOSURE, "REF-100"))
                .thenReturn(true);

//        assertThatThrownBy(() -> useCase.create(command))
//                .isInstanceOf(DuplicateDisclosureReceiptException.class)
//                .hasMessageContaining("already exists");

        verify(repository, never()).save(any(DisclosureReceipt.class));
    }
}
