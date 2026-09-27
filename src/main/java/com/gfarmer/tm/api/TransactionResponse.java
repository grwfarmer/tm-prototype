package com.gfarmer.tm.api;

import com.gfarmer.tm.domain.Transaction;
import com.gfarmer.tm.service.MonitoringService;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/** What the API returns for a transaction, including any alerts it raised. */
public record TransactionResponse(
        Long id,
        String transactionRef,
        String accountId,
        String counterpartyAccount,
        String counterpartyCountry,
        BigDecimal amount,
        String currency,
        Transaction.Direction direction,
        Instant bookedAt,
        List<AlertResponse> alerts) {

    public static TransactionResponse from(MonitoringService.Result result) {
        Transaction t = result.transaction();
        List<AlertResponse> alerts = result.alerts().stream().map(AlertResponse::from).toList();
        return new TransactionResponse(t.getId(), t.getTransactionRef(), t.getAccountId(),
                t.getCounterpartyAccount(), t.getCounterpartyCountry(), t.getAmount(),
                t.getCurrency(), t.getDirection(), t.getBookedAt(), alerts);
    }
}
