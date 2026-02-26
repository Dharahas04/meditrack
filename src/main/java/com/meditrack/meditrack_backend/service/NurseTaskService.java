package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.dto.NurseTaskCreateDTO;
import com.meditrack.meditrack_backend.model.NurseTask;
import com.meditrack.meditrack_backend.model.Patient;
import com.meditrack.meditrack_backend.model.Prescription;
import com.meditrack.meditrack_backend.model.User;
import com.meditrack.meditrack_backend.repository.NurseTaskRepository;
import com.meditrack.meditrack_backend.repository.PatientRepository;
import com.meditrack.meditrack_backend.repository.PrescriptionRepository;
import com.meditrack.meditrack_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NurseTaskService {

    private final NurseTaskRepository nurseTaskRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final PrescriptionRepository prescriptionRepository;

    public NurseTask createTask(NurseTaskCreateDTO dto) {
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        User nurse = userRepository.findById(dto.getNurseId())
                .orElseThrow(() -> new RuntimeException("Nurse not found"));

        if (nurse.getRole() != User.Role.NURSE) {
            throw new RuntimeException("Assigned user must be NURSE");
        }

        User doctor = userRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        if (doctor.getRole() != User.Role.DOCTOR) {
            throw new RuntimeException("Task creator must be DOCTOR");
        }

        Prescription prescription = null;
        if (dto.getPrescriptionId() != null) {
            prescription = prescriptionRepository.findById(dto.getPrescriptionId())
                    .orElseThrow(() -> new RuntimeException("Prescription not found"));
        }

        NurseTask task = new NurseTask();
        task.setPatient(patient);
        task.setAssignedNurse(nurse);
        task.setCreatedByDoctor(doctor);
        task.setPrescription(prescription);
        task.setTaskType(dto.getTaskType() != null ? dto.getTaskType() : NurseTask.TaskType.MEDICATION);
        task.setTitle(dto.getTitle());
        task.setDetails(dto.getDetails());
        task.setDueAt(dto.getDueAt());

        return nurseTaskRepository.save(task);
    }

    public List<NurseTask> getByPatient(Integer patientId) {
        return nurseTaskRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
    }

    public List<NurseTask> getByNurse(Integer nurseId) {
        return nurseTaskRepository.findByAssignedNurseIdOrderByCreatedAtDesc(nurseId);
    }

    public List<NurseTask> getPendingByNurse(Integer nurseId) {
        return nurseTaskRepository.findByAssignedNurseIdAndStatusOrderByDueAtAsc(
                nurseId, NurseTask.TaskStatus.PENDING);
    }

    public NurseTask markCompleted(Integer taskId, Integer nurseId, String notes) {
        NurseTask task = nurseTaskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getAssignedNurse().getId().equals(nurseId)) {
            throw new RuntimeException("Only assigned nurse can complete this task");
        }

        task.setStatus(NurseTask.TaskStatus.COMPLETED);
        task.setCompletedAt(LocalDateTime.now());
        task.setCompletionNotes(notes);

        return nurseTaskRepository.save(task);
    }

    public NurseTask markMissed(Integer taskId, Integer nurseId, String notes) {
        NurseTask task = nurseTaskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getAssignedNurse().getId().equals(nurseId)) {
            throw new RuntimeException("Only assigned nurse can update this task");
        }

        task.setStatus(NurseTask.TaskStatus.MISSED);
        task.setCompletedAt(LocalDateTime.now());
        task.setCompletionNotes(notes);

        return nurseTaskRepository.save(task);
    }
}
