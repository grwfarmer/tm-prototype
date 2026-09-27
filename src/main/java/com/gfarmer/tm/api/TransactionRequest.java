package com.gfarmer.tm.api;

import com.gfarmer.tm.domain.Transaction;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.Instant;

/** The JSON body a client sends to submit a transaction for monitoring. */
public record TransactionRequest(
        @NotBlank @Size(max = 64) String transactionRef,
        @NotBlank @Size(max = 34) String accountId,
        @Size(max = 34) String counterpartyAccount,
        @Pattern(regexp = "[A-Z]{2}") String counterpartyCountry,
        @NotNull @Positive @Digits(integer = 15, fraction = 4) BigDecimal amount,
        @NotBlank @Pattern(regexp = "[A-Z]{3}") String currency,
        @NotNull Transaction.Direction direction,
        @NotNull Instant bookedAt) {

    public Transaction toTransaction() {
        return new Transaction(transactionRef, accountId, counterpartyAccount,
                counterpartyCountry, amount, currency, direction, bookedAt);
    }
}
