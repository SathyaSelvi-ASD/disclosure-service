package com.vbox.disclosure.api.dto.response;

import com.vbox.disclosure.api.dto.request.DisclosureDto;

import java.util.List;

public record SearchResponseDto(List<DisclosureDto> items, int page, int pageSize,
                                long totalElements, int totalPages) {
}
