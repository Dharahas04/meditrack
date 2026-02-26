package com.meditrack.meditrack_backend.dto;

import lombok.Data;

@Data
public class PatientRequestActionDTO {
    private Integer processedByUserId;
    private String remarks;
}
