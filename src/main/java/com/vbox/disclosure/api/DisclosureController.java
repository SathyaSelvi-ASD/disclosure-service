package com.vbox.disclosure.api;

import com.vbox.disclosure.api.dto.request.CreateDisclosureDto;
import com.vbox.disclosure.api.dto.request.DisclosureDto;
import com.vbox.disclosure.api.dto.request.DisclosureSearchDto;
import com.vbox.disclosure.api.dto.response.ApiResponse;
import com.vbox.disclosure.api.dto.response.SearchResponseDto;
import com.vbox.disclosure.application.DisclosureUseCase;
import com.vbox.disclosure.i18n.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/disclosures/v1")
@RequiredArgsConstructor
public class DisclosureController {
    private final DisclosureUseCase useCase;
    private final MessageResolver messages;

    @GetMapping("/greet")
    public ResponseEntity<String> greet() {
        log.info("Processing greet request");
        return ResponseEntity.ok(messages.get(MessageKey.GREETING, LocaleContextHolder.getLocale()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody CreateDisclosureDto request) {
        log.info("Processing create disclosure request referenceNumber={}", request.referenceNumber());
        DisclosureDto created = useCase.create(request);
        ApiResponse response = new ApiResponse("SUCCESS", HttpStatus.OK.value(), "", List.of(), List.of(), created);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse> search(@Valid @RequestBody DisclosureSearchDto request) {
        log.info("Processing disclosure search request");
        SearchResponseDto result = useCase.search(request);
        ApiResponse response = new ApiResponse("SUCCESS", HttpStatus.OK.value(), "", List.of(), List.of(), result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/work-action")
    public ResponseEntity<ApiResponse> searchWorkAction(
            @RequestParam("work_action_referenceid") String workActionReferenceId) {
        log.info("Processing work action search request workActionReferenceId={}", workActionReferenceId);
        ApiResponse response =
                useCase.searchWorkAction(workActionReferenceId);
        return ResponseEntity.ok(response);
    }
}
