package com.vbox.disclosure.api;

import com.vbox.disclosure.api.dto.request.CreateDisclosureReceiptRequest;
import com.vbox.disclosure.api.dto.request.DisclosureReceiptSearchRequest;
import com.vbox.disclosure.api.dto.response.ApiResponse;
import com.vbox.disclosure.api.dto.response.CreateDisclosureReceiptResponse;
import com.vbox.disclosure.api.dto.response.DisclosureReceiptResponse;
import com.vbox.disclosure.api.dto.response.DisclosureReceiptSearchResponse;
import com.vbox.disclosure.application.CreateDisclosureReceiptCommand;
import com.vbox.disclosure.application.CreateDisclosureReceiptResult;
import com.vbox.disclosure.application.CreateDisclosureReceiptUseCase;
import com.vbox.disclosure.application.DisclosureReceiptSearchCommand;
import com.vbox.disclosure.application.DisclosureReceiptSearchResult;
import com.vbox.disclosure.application.DisclosureReceiptSearchUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.ObjectProvider;
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
    private final ObjectProvider<DisclosureReceiptSearchUseCase> searchUseCaseProvider;

    public DisclosureReceiptController(
            CreateDisclosureReceiptUseCase useCase,
            ObjectProvider<DisclosureReceiptSearchUseCase> searchUseCaseProvider) {
        this.useCase = useCase;
        this.searchUseCaseProvider = searchUseCaseProvider;
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

    @PostMapping("/disclosure-receipts/search")
    public ResponseEntity<ApiResponse> searchDisclosureReceipts(
            @Valid @RequestBody DisclosureReceiptSearchRequest request) {
        DisclosureReceiptSearchResult result = searchUseCaseProvider.getObject()
                .search(DisclosureReceiptSearchCommand.fromRequest(request));

        List<DisclosureReceiptResponse> receipts = result.disclosureReceipts().stream()
                .map(receipt -> new DisclosureReceiptResponse(
                        receipt.disclosureReceiptId(), receipt.workActionId(), receipt.customerId(), receipt.receiptType(),
                        receipt.deliveryChannel(), receipt.recipient(), receipt.receivedAt(), receipt.referenceNumber(),
                        receipt.status()))
                .toList();
        DisclosureReceiptSearchResponse payload = new DisclosureReceiptSearchResponse(receipts);
        String message = receipts.isEmpty() ? "No disclosure receipts found." : "Disclosure receipts retrieved successfully.";

        return ResponseEntity.ok(new ApiResponse("SUCCESS", HttpStatus.OK.value(), message, List.of(), List.of(), payload));
    }
}
