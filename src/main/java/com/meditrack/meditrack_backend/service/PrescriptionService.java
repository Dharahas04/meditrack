package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.dto.PrescriptionCreateDTO;
import com.meditrack.meditrack_backend.model.Patient;
import com.meditrack.meditrack_backend.model.Prescription;
import com.meditrack.meditrack_backend.model.User;
import com.meditrack.meditrack_backend.repository.PatientRepository;
import com.meditrack.meditrack_backend.repository.PrescriptionRepository;
import com.meditrack.meditrack_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public Prescription createPrescription(PrescriptionCreateDTO dto) {
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        User doctor = userRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        if (doctor.getRole() != User.Role.DOCTOR) {
            throw new RuntimeException("Prescriber must be DOCTOR");
        }

        User nurse = null;
        if (dto.getNurseId() != null) {
            nurse = userRepository.findById(dto.getNurseId())
                    .orElseThrow(() -> new RuntimeException("Nurse not found"));

            if (nurse.getRole() != User.Role.NURSE) {
                throw new RuntimeException("Assigned user must be NURSE");
            }
        }

        Prescription p = new Prescription();
        p.setPatient(patient);
        p.setPrescribedByDoctor(doctor);
        p.setAssignedNurse(nurse);
        p.setMedicationName(dto.getMedicationName());
        p.setDosage(dto.getDosage());
        p.setFrequency(dto.getFrequency());
        p.setRoute(dto.getRoute());
        p.setInstructions(dto.getInstructions());
        p.setStartDate(dto.getStartDate());
        p.setEndDate(dto.getEndDate());

        return prescriptionRepository.save(p);
    }

    public List<Prescription> getByPatient(Integer patientId) {
        return prescriptionRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
    }

    public List<Prescription> getByDoctor(Integer doctorId) {
        return prescriptionRepository.findByPrescribedByDoctorIdOrderByCreatedAtDesc(doctorId);
    }

    public List<Prescription> getByNurse(Integer nurseId) {
        return prescriptionRepository.findByAssignedNurseIdOrderByCreatedAtDesc(nurseId);
    }

    public Prescription stopPrescription(Integer prescriptionId) {
        Prescription p = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
        p.setStatus(Prescription.PrescriptionStatus.STOPPED);
        return prescriptionRepository.save(p);
    }

    public Prescription completePrescription(Integer prescriptionId) {
        Prescription p = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
        p.setStatus(Prescription.PrescriptionStatus.COMPLETED);
        return prescriptionRepository.save(p);
    }
}
