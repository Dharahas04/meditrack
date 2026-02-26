package com.meditrack.meditrack_backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "nurse_tasks")
public class NurseTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "assigned_nurse_id", nullable = false)
    private User assignedNurse;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private User createdByDoctor;

    @ManyToOne
    @JoinColumn(name = "prescription_id")
    private Prescription prescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskType taskType = TaskType.MEDICATION;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String details;

    private LocalDateTime dueAt;

    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.PENDING;

    private LocalDateTime completedAt;

    @Column(length = 1000)
    private String completionNotes;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum TaskType {
        MEDICATION, VITALS, INJECTION, DRESSING, OTHER
    }

    public enum TaskStatus {
        PENDING, IN_PROGRESS, COMPLETED, MISSED
    }
}
