package com.gfarmer.tm.rules;

import com.gfarmer.tm.domain.Alert;
import com.gfarmer.tm.domain.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LargeAmountRuleTest {

    private final LargeAmountRule rule = new LargeAmountRule(new BigDecimal("10000"));

    @Test
    void raisesHighSeverityAlertAtThreshold() {
        Optional<MonitoringRule.Finding> finding = rule.evaluate(transactionOf("10000.00"));

        assertTrue(finding.isPresent());
        assertEquals(LargeAmountRule.RULE_CODE, finding.get().ruleCode());
        assertEquals(Alert.Severity.HIGH, finding.get().severity());
    }

    @Test
    void ignoresAmountsBelowThreshold() {
        assertTrue(rule.evaluate(transactionOf("9999.99")).isEmpty());
    }

    private static Transaction transactionOf(String amount) {
        return new Transaction("TX-TEST", "GB00TEST0001", null, null,
                new BigDecimal(amount), "GBP", Transaction.Direction.DEBIT, Instant.now());
    }
}
