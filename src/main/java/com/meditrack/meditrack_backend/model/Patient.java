package com.meditrack.meditrack_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String phone;

    private String email;

    @Column(name = "blood_group")
    private String bloodGroup;

    @Column(name = "condition_summary")
    private String conditionSummary;

    @ManyToOne
    @JoinColumn(name = "assigned_doctor_id")
    private User assignedDoctor;

    @ManyToOne
    @JoinColumn(name = "bed_id")
    private Bed bed;

    @Column(name = "admission_date")
    private LocalDate admissionDate;

    @Column(name = "discharge_date")
    private LocalDate dischargeDate;

    @Enumerated(EnumType.STRING)
    private PatientStatus status = PatientStatus.ADMITTED;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Gender {
        MALE, FEMALE, OTHER
    }

    public enum PatientStatus {
        ADMITTED, DISCHARGED, CRITICAL
    }
}