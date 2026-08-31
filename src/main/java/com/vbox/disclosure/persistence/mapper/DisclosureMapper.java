package com.vbox.disclosure.persistence.mapper;

import com.vbox.disclosure.api.dto.request.CreateDisclosureDto;
import com.vbox.disclosure.api.dto.request.DisclosureDto;
import com.vbox.disclosure.domain.Disclosure;
import com.vbox.disclosure.persistence.DisclosureEntity;
import lombok.extern.slf4j.Slf4j;
import java.time.Instant;

@Slf4j
public final class DisclosureMapper {
    private DisclosureMapper() {}
    public static DisclosureEntity toEntity(CreateDisclosureDto dto) {
        log.info("Mapping create disclosure request for referenceNumber={}", dto.referenceNumber());
        return new DisclosureEntity(null, dto.referenceNumber(), dto.customerId(), dto.description(), "DRAFT", Instant.now());
    }
    public static Disclosure toDomain(DisclosureEntity entity) {
        return new Disclosure(entity.getId(), entity.getReferenceNumber(), entity.getCustomerId(), entity.getDescription(), entity.getStatus(), entity.getCreatedAt());
    }
    public static DisclosureDto toDto(Disclosure domain) {
        return new DisclosureDto(domain.id(), domain.referenceNumber(), domain.customerId(), domain.description(), domain.status(), domain.createdAt());
    }
}
