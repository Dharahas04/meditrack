package com.meditrack.meditrack_backend.repository;

import com.meditrack.meditrack_backend.model.NurseTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NurseTaskRepository extends JpaRepository<NurseTask, Integer> {
    List<NurseTask> findByPatientIdOrderByCreatedAtDesc(Integer patientId);

    List<NurseTask> findByAssignedNurseIdOrderByCreatedAtDesc(Integer nurseId);

    List<NurseTask> findByAssignedNurseIdAndStatusOrderByDueAtAsc(Integer nurseId, NurseTask.TaskStatus status);
}
