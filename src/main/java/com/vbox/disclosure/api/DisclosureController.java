package com.vbox.disclosure.api;

import com.vbox.disclosure.api.dto.request.CreateDisclosureDto;
import com.vbox.disclosure.api.dto.request.DisclosureDto;
import com.vbox.disclosure.api.dto.request.DisclosureSearchDto;
import com.vbox.disclosure.api.dto.request.ReceiptDisplayRequest;
import com.vbox.disclosure.api.dto.response.ApiResponse;
import com.vbox.disclosure.api.dto.response.ReceiptDisplayResponse;
import com.vbox.disclosure.application.DisclosureUseCase;
import com.vbox.disclosure.application.ReceiptDisplayUseCase;
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
    private final ReceiptDisplayUseCase receiptDisplayUseCase;
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
        ApiResponse response = useCase.search(request);
        return ResponseEntity.status(HttpStatus.valueOf(response.statusCode())).body(response);
    }

    @PostMapping("/receipt/display")
    public ResponseEntity<ApiResponse> displayReceipt(@Valid @RequestBody ReceiptDisplayRequest request) {
        ReceiptDisplayResponse result = receiptDisplayUseCase.display(request);
        ApiResponse response = new ApiResponse("SUCCESS", HttpStatus.ACCEPTED.value(),
                "Receipt display event published.", List.of(), List.of(), result);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
