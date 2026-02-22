package com.meditrack.meditrack_backend.repository;

import com.meditrack.meditrack_backend.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    List<Patient> findByStatus(Patient.PatientStatus status);

    List<Patient> findByAssignedDoctorId(Integer doctorId);
}