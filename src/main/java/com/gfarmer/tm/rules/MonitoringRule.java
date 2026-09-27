package com.gfarmer.tm.rules;

import com.gfarmer.tm.domain.Alert;
import com.gfarmer.tm.domain.Transaction;

import java.util.Optional;

/**
 * A single detection rule. Each rule looks at a transaction and either
 * returns a finding (which becomes an alert) or nothing.
 */
public interface MonitoringRule {

    record Finding(String ruleCode, Alert.Severity severity, String reason) { }

    Optional<Finding> evaluate(Transaction transaction);
}
