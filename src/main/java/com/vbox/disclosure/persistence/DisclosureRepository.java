package com.vbox.disclosure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DisclosureRepository extends JpaRepository<DisclosureEntity, Long>, JpaSpecificationExecutor<DisclosureEntity> {
    boolean existsByReferenceNumber(String referenceNumber);
}
