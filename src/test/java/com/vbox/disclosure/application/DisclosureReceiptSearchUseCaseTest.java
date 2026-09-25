package com.vbox.disclosure.application;

import com.vbox.disclosure.application.exception.InvalidDisclosureReceiptException;
import com.vbox.disclosure.domain.DeliveryChannel;
import com.vbox.disclosure.domain.DisclosureReceiptRepository;
import com.vbox.disclosure.domain.DisclosureReceiptStatus;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class DisclosureReceiptSearchUseCaseTest {
    private final DisclosureReceiptRepository repository = mock(DisclosureReceiptRepository.class);
    private final DisclosureReceiptSearchUseCase useCase = new DisclosureReceiptSearchUseCase(repository);

    @Test
    void shouldReturnReceiptsForMultipleCriteria() {
        DisclosureReceiptSearchCommand command = new DisclosureReceiptSearchCommand(
                null, "12345", null, DeliveryChannel.EMAIL, DisclosureReceiptStatus.CREATED, null,
                Instant.parse("2026-09-01T00:00:00Z"), Instant.parse("2026-09-08T23:59:59Z"));
        when(repository.search(command)).thenReturn(List.of());

        DisclosureReceiptSearchResult result = useCase.search(command);

        assertThat(result.disclosureReceipts()).isEmpty();
        verify(repository).search(command);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldRejectEmptySearchCriteria() {
        DisclosureReceiptSearchCommand command = new DisclosureReceiptSearchCommand(
                null, null, null, null, null, null, null, null);

        assertThatThrownBy(() -> useCase.search(command))
                .isInstanceOf(InvalidDisclosureReceiptException.class)
                .hasMessage("At least one search criterion must be provided");
        verifyNoInteractions(repository);
    }

    @Test
    void shouldRejectInvalidDateRange() {
        DisclosureReceiptSearchCommand command = new DisclosureReceiptSearchCommand(
                "WA10001", null, null, null, null, null,
                Instant.parse("2026-09-09T00:00:00Z"), Instant.parse("2026-09-08T00:00:00Z"));

        assertThatThrownBy(() -> useCase.search(command))
                .isInstanceOf(InvalidDisclosureReceiptException.class)
                .hasMessage("fromReceivedAt cannot be after toReceivedAt");
        verifyNoInteractions(repository);
    }
}
