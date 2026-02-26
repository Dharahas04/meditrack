package com.meditrack.meditrack_backend.repository;

import com.meditrack.meditrack_backend.model.PatientRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRequestRepository extends JpaRepository<PatientRequest, Integer> {
    List<PatientRequest> findByStatus(PatientRequest.RequestStatus status);

    List<PatientRequest> findByRequestedByDoctorId(Integer doctorId);
}
