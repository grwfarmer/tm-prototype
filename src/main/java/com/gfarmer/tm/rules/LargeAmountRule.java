package com.gfarmer.tm.rules;

import com.gfarmer.tm.domain.Alert;
import com.gfarmer.tm.domain.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Flags any single transaction at or above a configured amount.
 * Simplification: assumes one currency. A real system would convert
 * to a base currency first.
 */
@Component
public class LargeAmountRule implements MonitoringRule {

    public static final String RULE_CODE = "LARGE_AMOUNT";

    private final BigDecimal threshold;

    public LargeAmountRule(@Value("${tm.rules.large-amount.threshold}") BigDecimal threshold) {
        this.threshold = threshold;
    }

    @Override
    public Optional<Finding> evaluate(Transaction transaction) {
        if (transaction.getAmount().compareTo(threshold) < 0) {
            return Optional.empty();
        }
        String reason = "Amount %s %s is at or above the %s threshold".formatted(
                transaction.getAmount().toPlainString(),
                transaction.getCurrency(),
                threshold.toPlainString());
        return Optional.of(new Finding(RULE_CODE, Alert.Severity.HIGH, reason));
    }
}
