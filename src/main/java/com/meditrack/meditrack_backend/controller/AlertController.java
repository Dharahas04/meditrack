package com.meditrack.meditrack_backend.controller;

import com.meditrack.meditrack_backend.model.Alert;
import com.meditrack.meditrack_backend.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AlertController {

    private final AlertService alertService;

    // Get all active alerts
    @GetMapping
    public ResponseEntity<List<Alert>> getAllActiveAlerts() {
        return ResponseEntity.ok(alertService.getAllActiveAlerts());
    }

    // Get alerts by severity
    @GetMapping("/severity/{severity}")
    public ResponseEntity<List<Alert>> getBySeverity(
            @PathVariable Alert.Severity severity) {
        return ResponseEntity.ok(
                alertService.getAlertsBySeverity(severity));
    }

    // Create alert
    @PostMapping
    public ResponseEntity<Alert> createAlert(
            @RequestBody Alert alert) {
        return ResponseEntity.ok(alertService.createAlert(alert));
    }

    // Resolve alert
    @PutMapping("/{id}/resolve")
    public ResponseEntity<Alert> resolveAlert(
            @PathVariable Integer id) {
        return ResponseEntity.ok(alertService.resolveAlert(id));
    }
}