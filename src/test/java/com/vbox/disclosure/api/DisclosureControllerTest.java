package com.vbox.disclosure.api;

import com.vbox.disclosure.application.DisclosureUseCase;
import com.vbox.disclosure.i18n.MessageResolver;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Locale;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

@WebMvcTest(DisclosureController.class)
class DisclosureControllerTest {
    @Autowired MockMvc mockMvc;
    @MockitoBean DisclosureUseCase useCase;
    @MockitoBean MessageResolver messages;

    @Test
    void greetReturnsLocalizedGreeting() throws Exception {
        when(messages.get(any(), any(Locale.class))).thenReturn("Welcome to the VBOX Disclosure Service.");
        mockMvc.perform(get("/api/disclosures/v1/greet").header("Accept-Language", "en"))
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome to the VBOX Disclosure Service."));
    }
}
