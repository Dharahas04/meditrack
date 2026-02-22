package com.meditrack.meditrack_backend.repository;

import com.meditrack.meditrack_backend.model.Bed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BedRepository extends JpaRepository<Bed, Integer> {
    List<Bed> findByStatus(Bed.BedStatus status);

    List<Bed> findByWardId(Integer wardId);
}