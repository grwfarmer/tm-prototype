package com.gfarmer.tm.api;

import com.gfarmer.tm.domain.Alert;

import java.time.Instant;

/** What the API returns for an alert. */
public record AlertResponse(
        Long id,
        Long transactionId,
        String ruleCode,
        Alert.Severity severity,
        Alert.Status status,
        String reason,
        Instant createdAt) {

    public static AlertResponse from(Alert a) {
        return new AlertResponse(a.getId(), a.getTransactionId(), a.getRuleCode(),
                a.getSeverity(), a.getStatus(), a.getReason(), a.getCreatedAt());
    }
}
