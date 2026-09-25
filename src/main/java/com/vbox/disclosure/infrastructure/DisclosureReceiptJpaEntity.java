package com.vbox.disclosure.infrastructure;

import com.vbox.disclosure.domain.DeliveryChannel;
import com.vbox.disclosure.domain.DisclosureReceiptStatus;
import com.vbox.disclosure.domain.ReceiptType;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "disclosure_receipt", indexes = {
        @Index(name = "idx_disclosure_receipt_work_action_type_ref", columnList = "work_action_id, receipt_type, reference_number", unique = true)
})
public class DisclosureReceiptJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "disclosure_receipt_id", nullable = false, unique = true, length = 50)
    private String disclosureReceiptId;

    @Column(name = "work_action_id", nullable = false, length = 100)
    private String workActionId;

    @Column(name = "customer_id", nullable = false, length = 100)
    private String customerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "receipt_type", nullable = false, length = 50)
    private ReceiptType receiptType;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_channel", nullable = false, length = 50)
    private DeliveryChannel deliveryChannel;

    @Column(name = "recipient", nullable = false, length = 255)
    private String recipient;

    @Column(name = "received_at", nullable = false)
    private Instant receivedAt;

    @Column(name = "reference_number", nullable = false, length = 255)
    private String referenceNumber;

    @Lob
    @Column(name = "notes")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private DisclosureReceiptStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected DisclosureReceiptJpaEntity() {
    }

    public DisclosureReceiptJpaEntity(
            String disclosureReceiptId,
            String workActionId,
            String customerId,
            ReceiptType receiptType,
            DeliveryChannel deliveryChannel,
            String recipient,
            Instant receivedAt,
            String referenceNumber,
            String notes,
            DisclosureReceiptStatus status,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.disclosureReceiptId = disclosureReceiptId;
        this.workActionId = workActionId;
        this.customerId = customerId;
        this.receiptType = receiptType;
        this.deliveryChannel = deliveryChannel;
        this.recipient = recipient;
        this.receivedAt = receivedAt;
        this.referenceNumber = referenceNumber;
        this.notes = notes;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public String getDisclosureReceiptId() { return disclosureReceiptId; }
    public String getWorkActionId() { return workActionId; }
    public String getCustomerId() { return customerId; }
    public ReceiptType getReceiptType() { return receiptType; }
    public DeliveryChannel getDeliveryChannel() { return deliveryChannel; }
    public String getRecipient() { return recipient; }
    public Instant getReceivedAt() { return receivedAt; }
    public String getReferenceNumber() { return referenceNumber; }
    public String getNotes() { return notes; }
    public DisclosureReceiptStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
