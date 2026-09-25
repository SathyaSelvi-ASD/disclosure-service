package com.vbox.disclosure.infrastructure;

import com.vbox.disclosure.application.DisclosureReceiptSearchCommand;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DisclosureReceiptRepositoryAdapterTest {

    @Test
    void shouldSearchWhenOnlyOneOptionalCriterionIsProvided() {
        SpringDataDisclosureReceiptRepository repository = mock(SpringDataDisclosureReceiptRepository.class);
        when(repository.findAll(any(org.springframework.data.jpa.domain.Specification.class))).thenReturn(List.of());
        DisclosureReceiptRepositoryAdapter adapter = new DisclosureReceiptRepositoryAdapter(repository);
        DisclosureReceiptSearchCommand command = new DisclosureReceiptSearchCommand(
                null, "CUST-101", null, null, null, null, null, null);

        assertDoesNotThrow(() -> adapter.search(command));
    }
}
