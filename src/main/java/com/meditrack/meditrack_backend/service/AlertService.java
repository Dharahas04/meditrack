package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.model.Alert;
import com.meditrack.meditrack_backend.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;

    public List<Alert> getAllActiveAlerts() {
        return alertRepository.findByIsResolvedFalse();
    }

    public Alert createAlert(Alert alert) {
        return alertRepository.save(alert);
    }

    public Alert resolveAlert(Integer id) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alert not found"));
        alert.setIsResolved(true);
        alert.setResolvedAt(LocalDateTime.now());
        return alertRepository.save(alert);
    }

    public List<Alert> getAlertsBySeverity(Alert.Severity severity) {
        return alertRepository.findBySeverity(severity);
    }
}