package com.meditrack.meditrack_backend.dto;

import com.meditrack.meditrack_backend.model.NurseTask;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NurseTaskCreateDTO {
    private Integer patientId;
    private Integer nurseId;
    private Integer doctorId;
    private Integer prescriptionId;
    private NurseTask.TaskType taskType;
    private String title;
    private String details;
    private LocalDateTime dueAt;
}
