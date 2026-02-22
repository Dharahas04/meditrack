package com.meditrack.meditrack_backend.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentDTO {
    private Integer patientId;
    private Integer doctorId;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String reason;
}