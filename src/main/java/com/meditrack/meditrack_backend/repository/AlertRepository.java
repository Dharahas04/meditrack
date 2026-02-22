package com.meditrack.meditrack_backend.repository;

import com.meditrack.meditrack_backend.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Integer> {
    List<Alert> findByIsResolvedFalse();

    List<Alert> findBySeverity(Alert.Severity severity);
}