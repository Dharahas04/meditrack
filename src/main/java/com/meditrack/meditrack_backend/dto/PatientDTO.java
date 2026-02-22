package com.meditrack.meditrack_backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PatientDTO {
    private String name;
    private Integer age;
    private String gender;
    private String phone;
    private String email;
    private String bloodGroup;
    private String conditionSummary;
    private Integer assignedDoctorId;
    private Integer bedId;
    private LocalDate admissionDate;
}