package com.gfarmer.tm.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "alerts")
public class Alert {

    public enum Severity { LOW, MEDIUM, HIGH }

    public enum Status { OPEN, UNDER_REVIEW, ESCALATED, CLOSED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_id", nullable = false)
    private Long transactionId;

    @Column(name = "rule_code", nullable = false)
    private String ruleCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severity severity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private String reason;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected Alert() {
        // Required by JPA
    }

    public Alert(Long transactionId, String ruleCode, Severity severity, String reason) {
        this.transactionId = transactionId;
        this.ruleCode = ruleCode;
        this.severity = severity;
        this.reason = reason;
        this.status = Status.OPEN;
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public Long getTransactionId() { return transactionId; }
    public String getRuleCode() { return ruleCode; }
    public Severity getSeverity() { return severity; }
    public Status getStatus() { return status; }
    public String getReason() { return reason; }
    public Instant getCreatedAt() { return createdAt; }
}
