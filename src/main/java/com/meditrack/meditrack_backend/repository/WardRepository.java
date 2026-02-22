package com.meditrack.meditrack_backend.repository;

import com.meditrack.meditrack_backend.model.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WardRepository extends JpaRepository<Ward, Integer> {
}