package com.gfarmer.tm.service;

import com.gfarmer.tm.domain.Alert;
import com.gfarmer.tm.domain.AlertRepository;
import com.gfarmer.tm.domain.Transaction;
import com.gfarmer.tm.domain.TransactionRepository;
import com.gfarmer.tm.rules.MonitoringRule;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class MonitoringService {

    /** A transaction together with any alerts raised against it. */
    public record Result(Transaction transaction, List<Alert> alerts) { }

    private final TransactionRepository transactions;
    private final AlertRepository alerts;
    private final List<MonitoringRule> rules;

    // Spring injects every MonitoringRule bean, so new rules are picked up automatically
    public MonitoringService(TransactionRepository transactions,
                             AlertRepository alerts,
                             List<MonitoringRule> rules) {
        this.transactions = transactions;
        this.alerts = alerts;
        this.rules = rules;
    }

    @Transactional
    public Result submit(Transaction transaction) {
        if (transactions.existsByTransactionRef(transaction.getTransactionRef())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Transaction " + transaction.getTransactionRef() + " has already been submitted");
        }
        Transaction saved = transactions.save(transaction);

        List<Alert> raised = rules.stream()
                .map(rule -> rule.evaluate(saved))
                .flatMap(Optional::stream)
                .map(finding -> alerts.save(
                        new Alert(saved.getId(), finding.ruleCode(), finding.severity(), finding.reason())))
                .toList();

        return new Result(saved, raised);
    }

    @Transactional(readOnly = true)
    public Result get(Long id) {
        Transaction transaction = transactions.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Transaction " + id + " not found"));
        return new Result(transaction, alerts.findByTransactionId(id));
    }

    @Transactional(readOnly = true)
    public List<Alert> alertsWithStatus(Alert.Status status) {
        return alerts.findByStatusOrderByCreatedAtDesc(status);
    }
}
