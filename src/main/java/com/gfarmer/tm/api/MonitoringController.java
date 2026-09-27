package com.gfarmer.tm.api;

import com.gfarmer.tm.domain.Alert;
import com.gfarmer.tm.service.MonitoringService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MonitoringController {

    private final MonitoringService service;

    public MonitoringController(MonitoringService service) {
        this.service = service;
    }

    @PostMapping("/transactions")
    public ResponseEntity<TransactionResponse> submit(@Valid @RequestBody TransactionRequest request) {
        TransactionResponse body = TransactionResponse.from(service.submit(request.toTransaction()));
        return ResponseEntity.created(URI.create("/api/transactions/" + body.id())).body(body);
    }

    @GetMapping("/transactions/{id}")
    public TransactionResponse get(@PathVariable Long id) {
        return TransactionResponse.from(service.get(id));
    }

    @GetMapping("/alerts")
    public List<AlertResponse> alerts(@RequestParam(defaultValue = "OPEN") Alert.Status status) {
        return service.alertsWithStatus(status).stream().map(AlertResponse::from).toList();
    }
}
