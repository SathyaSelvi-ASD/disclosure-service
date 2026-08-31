package com.vbox.disclosure.application;

import com.vbox.disclosure.api.dto.request.CreateDisclosureDto;
import com.vbox.disclosure.api.dto.request.DisclosureDto;
import com.vbox.disclosure.api.dto.request.DisclosureSearchDto;
import com.vbox.disclosure.api.dto.response.SearchResponseDto;
import com.vbox.disclosure.application.exception.DisclosurePersistenceException;
import com.vbox.disclosure.application.exception.DuplicateDisclosureException;
import com.vbox.disclosure.domain.Disclosure;
import com.vbox.disclosure.persistence.DisclosureEntity;
import com.vbox.disclosure.persistence.DisclosureRepository;
import com.vbox.disclosure.persistence.mapper.DisclosureMapper;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class DisclosureUseCase {
    private final DisclosureRepository repository;

    @Transactional
    public DisclosureDto create(CreateDisclosureDto request) {
        log.info("Creating disclosure referenceNumber={}", request.referenceNumber());
        if (repository.existsByReferenceNumber(request.referenceNumber())) {
            throw new DuplicateDisclosureException(request.referenceNumber());
        }
        try {
            Disclosure saved = DisclosureMapper.toDomain(repository.save(DisclosureMapper.toEntity(request)));
            log.info("Disclosure created id={} status={}", saved.id(), saved.status());
            return DisclosureMapper.toDto(saved);
        } catch (DataAccessException ex) {
            log.error("Database error while saving disclosure referenceNumber={}", request.referenceNumber(), ex);
            throw new DisclosurePersistenceException("Disclosure creation failed.", ex);
        }
    }

    @Transactional(readOnly = true)
    public SearchResponseDto search(DisclosureSearchDto criteria) {
        log.info("Searching disclosures page={} pageSize={}", criteria.effectivePage(), criteria.effectivePageSize());
        Pageable pageable = PageRequest.of(criteria.effectivePage(), criteria.effectivePageSize(), Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<DisclosureEntity> result = repository.findAll((root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();
            if (hasText(criteria.referenceNumber())) predicates.add(cb.like(cb.lower(root.get("referenceNumber")), "%" + criteria.referenceNumber().toLowerCase() + "%"));
            if (hasText(criteria.customerId())) predicates.add(cb.equal(root.get("customerId"), criteria.customerId()));
            if (hasText(criteria.status())) predicates.add(cb.equal(root.get("status"), criteria.status().toUpperCase()));
            return cb.and(predicates.toArray(Predicate[]::new));
        }, pageable);
        var items = result.stream().map(DisclosureMapper::toDomain).map(DisclosureMapper::toDto).toList();
        log.info("Disclosure search completed totalElements={}", result.getTotalElements());
        return new SearchResponseDto(items, result.getNumber(), result.getSize(), result.getTotalElements(), result.getTotalPages());
    }
    private boolean hasText(String value) { return value != null && !value.isBlank(); }
}
