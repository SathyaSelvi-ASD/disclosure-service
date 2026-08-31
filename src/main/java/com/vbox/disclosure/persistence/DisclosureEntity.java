package com.vbox.disclosure.persistence;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "disclosures", uniqueConstraints = @UniqueConstraint(name = "uk_disclosure_reference", columnNames = "reference_number"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DisclosureEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "reference_number", nullable = false, length = 50)
    private String referenceNumber;
    @Column(name = "customer_id", nullable = false, length = 50)
    private String customerId;
    @Column(nullable = false, length = 500)
    private String description;
    @Column(nullable = false, length = 20)
    private String status;
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
