package com.gfarmer.tm.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {

    List<Alert> findByTransactionId(Long transactionId);

    List<Alert> findByStatusOrderByCreatedAtDesc(Alert.Status status);
}
