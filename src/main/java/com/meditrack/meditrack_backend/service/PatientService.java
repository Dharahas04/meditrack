package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.model.Patient;
import com.meditrack.meditrack_backend.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(Integer id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    public List<Patient> getPatientsByStatus(Patient.PatientStatus status) {
        return patientRepository.findByStatus(status);
    }

    public List<Patient> getPatientsByDoctor(Integer doctorId) {
        return patientRepository.findByAssignedDoctorId(doctorId);
    }

    public Patient createPatient(Patient patient) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean canCreate = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()) || "ROLE_RECEPTIONIST".equals(a.getAuthority()));

        if (!canCreate) {
            throw new AccessDeniedException(
                    "Only ADMIN/RECEPTIONIST can create patient directly. Doctors must create patient request.");
        }

        return patientRepository.save(patient);
    }

    public Patient updatePatient(Integer id, Patient patient) {
        Patient existing = getPatientById(id);
        existing.setName(patient.getName());
        existing.setAge(patient.getAge());
        existing.setConditionSummary(patient.getConditionSummary());
        existing.setStatus(patient.getStatus());
        existing.setAssignedDoctor(patient.getAssignedDoctor());
        existing.setBed(patient.getBed());
        return patientRepository.save(existing);
    }

    public void dischargePatient(Integer id) {
        Patient patient = getPatientById(id);
        patient.setStatus(Patient.PatientStatus.DISCHARGED);
        patient.getBed().setStatus(com.meditrack.meditrack_backend.model.Bed.BedStatus.AVAILABLE);
        patientRepository.save(patient);
    }
}