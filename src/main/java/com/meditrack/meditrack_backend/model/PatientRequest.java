package com.meditrack.meditrack_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "patient_requests")
public class PatientRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String patientName;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private Patient.Gender gender;

    private String phone;

    private String email;

    @Column(name = "blood_group")
    private String bloodGroup;

    @Column(name = "condition_summary")
    private String conditionSummary;

    @ManyToOne
    @JoinColumn(name = "requested_by_doctor_id", nullable = false)
    private User requestedByDoctor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status = RequestStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "processed_by_user_id")
    private User processedBy;

    @ManyToOne
    @JoinColumn(name = "created_patient_id")
    private Patient createdPatient;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    public enum RequestStatus {
        PENDING, APPROVED, REJECTED, REGISTERED
    }
}
