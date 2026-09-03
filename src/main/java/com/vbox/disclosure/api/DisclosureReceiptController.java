package com.vbox.disclosure.api;

import com.vbox.disclosure.api.dto.request.CreateDisclosureReceiptRequest;
import com.vbox.disclosure.api.dto.response.ApiResponse;
import com.vbox.disclosure.api.dto.response.CreateDisclosureReceiptResponse;
import com.vbox.disclosure.application.CreateDisclosureReceiptCommand;
import com.vbox.disclosure.application.CreateDisclosureReceiptResult;
import com.vbox.disclosure.application.CreateDisclosureReceiptUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/disclosures/v1")
public class DisclosureReceiptController {
    private final CreateDisclosureReceiptUseCase useCase;

    public DisclosureReceiptController(CreateDisclosureReceiptUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping("/disclosure-receipts")
    public ResponseEntity<ApiResponse> createDisclosureReceipt(
            @Valid @RequestBody CreateDisclosureReceiptRequest request) {
        CreateDisclosureReceiptResult result = useCase.create(CreateDisclosureReceiptCommand.fromRequest(request));

        CreateDisclosureReceiptResponse payload = new CreateDisclosureReceiptResponse(
                result.disclosureReceiptId(),
                result.workActionId(),
                result.customerId(),
                result.status()
        );

        ApiResponse response = new ApiResponse("SUCCESS", HttpStatus.OK.value(), "", List.of(), List.of(), payload);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
