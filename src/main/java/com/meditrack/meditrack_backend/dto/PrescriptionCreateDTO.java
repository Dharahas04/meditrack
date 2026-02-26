package com.meditrack.meditrack_backend.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PrescriptionCreateDTO {
    private Integer patientId;
    private Integer doctorId;
    private Integer nurseId;
    private String medicationName;
    private String dosage;
    private String frequency;
    private String route;
    private String instructions;
    private LocalDate startDate;
    private LocalDate endDate;
}
