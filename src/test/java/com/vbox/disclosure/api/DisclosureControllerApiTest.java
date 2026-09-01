package com.vbox.disclosure.api;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vbox.disclosure.api.dto.request.CreateDisclosureDto;
import com.vbox.disclosure.api.dto.request.DisclosureDto;
import com.vbox.disclosure.api.dto.request.DisclosureSearchDto;
import com.vbox.disclosure.api.dto.response.ApiResponse;
import com.vbox.disclosure.api.dto.response.SearchResponseDto;
import com.vbox.disclosure.application.DisclosureUseCase;
import com.vbox.disclosure.i18n.MessageResolver;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(DisclosureController.class)
@Import(DisclosureControllerApiTest.TestConfig.class)
class DisclosureControllerApiTest {

    @Autowired
    private MockMvc mockMvc;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DisclosureUseCase useCase;

    @MockitoBean
    private MessageResolver messageResolver;


    // ---------------------------------------------------------
    // POST /api/disclosures
    // ---------------------------------------------------------

    @Test
    void shouldCreateDisclosure() throws Exception {

        // Given
        CreateDisclosureDto request = new CreateDisclosureDto(
                "REF-12345",
                "CUST-101",
                "Basic disclosure"
        );

        DisclosureDto response = new DisclosureDto(
                1L,
                "REF-12345",
                "CUST-101",
                "Basic disclosure",
                "DRAFT",
                Instant.parse("2026-08-28T00:00:00Z")
        );

        when(useCase.create(any(CreateDisclosureDto.class)))
                .thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/disclosures/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value(""))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.warnings").isArray())
                .andExpect(jsonPath("$.data.id").value(1));

        // Verify
        verify(useCase)
                .create(any(CreateDisclosureDto.class));
    }


    // ---------------------------------------------------------
    // POST /api/disclosures/search
    // ---------------------------------------------------------

    @Test
    void shouldSearchDisclosures() throws Exception {

        // Given
        DisclosureSearchDto request = new DisclosureSearchDto(
                "REF-12345",
                "CUST-101",
                "DRAFT",
                0,
                20
        );

        ApiResponse response = new ApiResponse(
                "SUCCESS",
                200,
                "",
                List.of(),
                List.of(),
                new SearchResponseDto(
                        List.of(
                                new DisclosureDto(
                                        1L,
                                        "REF-12345",
                                        "CUST-101",
                                        "Basic disclosure",
                                        "DRAFT",
                                        Instant.parse("2026-08-28T00:00:00Z")
                                )
                        ),
                        0,
                        20,
                        1L,
                        1
                )
        );

        when(useCase.search(any(DisclosureSearchDto.class)))
                .thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/disclosures/v1/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value(""))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.warnings").isArray())
                .andExpect(jsonPath("$.data.items[0].id").value(1));

        // Verify
        verify(useCase)
                .search(any(DisclosureSearchDto.class));
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        ObjectMapper objectMapper() {
            return JsonMapper.builder()
                    .addModule(new JavaTimeModule())
                    .build();
        }
    }
}

