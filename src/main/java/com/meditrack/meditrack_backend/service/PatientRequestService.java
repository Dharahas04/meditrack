package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.dto.PatientRequestActionDTO;
import com.meditrack.meditrack_backend.dto.PatientRequestCreateDTO;
import com.meditrack.meditrack_backend.model.PatientRequest;
import com.meditrack.meditrack_backend.model.User;
import com.meditrack.meditrack_backend.repository.PatientRequestRepository;
import com.meditrack.meditrack_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.meditrack.meditrack_backend.model.Patient;
import com.meditrack.meditrack_backend.repository.PatientRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientRequestService {

    private final PatientRequestRepository patientRequestRepository;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;

    public PatientRequest createRequest(PatientRequestCreateDTO dto) {
        User doctor = userRepository.findById(dto.getRequestedByDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        if (doctor.getRole() != User.Role.DOCTOR) {
            throw new RuntimeException("Requested by user must have DOCTOR role");
        }

        PatientRequest req = new PatientRequest();
        req.setPatientName(dto.getPatientName());
        req.setAge(dto.getAge());
        req.setGender(dto.getGender());
        req.setPhone(dto.getPhone());
        req.setEmail(dto.getEmail());
        req.setBloodGroup(dto.getBloodGroup());
        req.setConditionSummary(dto.getConditionSummary());
        req.setRequestedByDoctor(doctor);
        req.setStatus(PatientRequest.RequestStatus.PENDING);

        return patientRequestRepository.save(req);
    }

    public List<PatientRequest> getAll() {
        return patientRequestRepository.findAll();
    }

    public List<PatientRequest> getByStatus(PatientRequest.RequestStatus status) {
        return patientRequestRepository.findByStatus(status);
    }

    public List<PatientRequest> getByDoctor(Integer doctorId) {
        return patientRequestRepository.findByRequestedByDoctorId(doctorId);
    }

    public PatientRequest approve(Integer requestId, PatientRequestActionDTO dto) {

        PatientRequest req = getById(requestId);
        User processor = getProcessor(dto.getProcessedByUserId());

        req.setStatus(PatientRequest.RequestStatus.APPROVED);
        req.setProcessedBy(processor);
        req.setRemarks(dto.getRemarks());
        req.setProcessedAt(LocalDateTime.now());

        // 🔥 CREATE PATIENT
        Patient patient = new Patient();
        patient.setName(req.getPatientName());
        patient.setAge(req.getAge());
        patient.setGender(Patient.Gender.valueOf(req.getGender().name()));
        patient.setPhone(req.getPhone());
        patient.setEmail(req.getEmail());
        patient.setBloodGroup(req.getBloodGroup());
        patient.setConditionSummary(req.getConditionSummary());
        patient.setAssignedDoctor(req.getRequestedByDoctor());
        patient.setAdmissionDate(LocalDate.now());
        patient.setStatus(Patient.PatientStatus.ADMITTED);

        // 🔥 SAVE PATIENT FIRST
        Patient savedPatient = patientRepository.save(patient);

        // 🔥 STORE CREATED PATIENT ID
        req.setCreatedPatient(savedPatient);
        return patientRequestRepository.save(req);
    }

    public PatientRequest reject(Integer requestId, PatientRequestActionDTO dto) {
        PatientRequest req = getById(requestId);
        User processor = getProcessor(dto.getProcessedByUserId());

        req.setStatus(PatientRequest.RequestStatus.REJECTED);
        req.setProcessedBy(processor);
        req.setRemarks(dto.getRemarks());
        req.setProcessedAt(LocalDateTime.now());

        return patientRequestRepository.save(req);
    }

    public PatientRequest markRegistered(Integer requestId, Integer processedByUserId) {
        PatientRequest req = getById(requestId);
        User processor = getProcessor(processedByUserId);

        req.setStatus(PatientRequest.RequestStatus.REGISTERED);
        req.setProcessedBy(processor);
        req.setProcessedAt(LocalDateTime.now());

        return patientRequestRepository.save(req);
    }

    private PatientRequest getById(Integer id) {
        return patientRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient request not found"));
    }

    private User getProcessor(Integer userId) {
        User processor = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Processor user not found"));

        if (processor.getRole() != User.Role.ADMIN && processor.getRole() != User.Role.RECEPTIONIST) {
            throw new RuntimeException("Only ADMIN/RECEPTIONIST can process patient requests");
        }

        return processor;
    }
}
