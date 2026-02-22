package com.meditrack.meditrack_backend.dto;

import lombok.Data;

@Data
public class AlertDTO {
    private String type;
    private String message;
    private Integer departmentId;
    private String severity;
}