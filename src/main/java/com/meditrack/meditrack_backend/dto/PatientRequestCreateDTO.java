package com.meditrack.meditrack_backend.dto;

import com.meditrack.meditrack_backend.model.Patient;
import lombok.Data;

@Data
public class PatientRequestCreateDTO {
    private String patientName;
    private Integer age;
    private Patient.Gender gender;
    private String phone;
    private String email;
    private String bloodGroup;
    private String conditionSummary;
    private Integer requestedByDoctorId;
}
