package com.vbox.disclosure.api.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record DisclosureSearchDto(String referenceNumber, String customerId, String status,
                                  @Min(0) Integer page,
                                  @Min(1) @Max(100) Integer pageSize) {
    public int effectivePage() { return page == null ? 0 : page; }
    public int effectivePageSize() { return pageSize == null ? 20 : pageSize; }
}
