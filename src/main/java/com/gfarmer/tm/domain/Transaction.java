package com.gfarmer.tm.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "transactions")
public class Transaction {

    public enum Direction { DEBIT, CREDIT }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_ref", nullable = false, unique = true)
    private String transactionRef;

    @Column(name = "account_id", nullable = false)
    private String accountId;

    @Column(name = "counterparty_account")
    private String counterpartyAccount;

    @Column(name = "counterparty_country")
    private String counterpartyCountry;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(nullable = false)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Direction direction;

    @Column(name = "booked_at", nullable = false)
    private Instant bookedAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected Transaction() {
        // Required by JPA
    }

    public Transaction(String transactionRef, String accountId, String counterpartyAccount,
                       String counterpartyCountry, BigDecimal amount, String currency,
                       Direction direction, Instant bookedAt) {
        this.transactionRef = transactionRef;
        this.accountId = accountId;
        this.counterpartyAccount = counterpartyAccount;
        this.counterpartyCountry = counterpartyCountry;
        this.amount = amount;
        this.currency = currency;
        this.direction = direction;
        this.bookedAt = bookedAt;
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public String getTransactionRef() { return transactionRef; }
    public String getAccountId() { return accountId; }
    public String getCounterpartyAccount() { return counterpartyAccount; }
    public String getCounterpartyCountry() { return counterpartyCountry; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public Direction getDirection() { return direction; }
    public Instant getBookedAt() { return bookedAt; }
    public Instant getCreatedAt() { return createdAt; }
}
