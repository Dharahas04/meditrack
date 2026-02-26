package com.meditrack.meditrack_backend.repository;

import com.meditrack.meditrack_backend.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {
    List<Prescription> findByPatientIdOrderByCreatedAtDesc(Integer patientId);

    List<Prescription> findByPrescribedByDoctorIdOrderByCreatedAtDesc(Integer doctorId);

    List<Prescription> findByAssignedNurseIdOrderByCreatedAtDesc(Integer nurseId);
}
