package com.meditrack.meditrack_backend.repository;

import com.meditrack.meditrack_backend.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    List<Attendance> findByUserId(Integer userId);

    List<Attendance> findByDate(LocalDate date);

    Optional<Attendance> findByUserIdAndDate(Integer userId, LocalDate date);

    Optional<Attendance> findTopByUserIdAndCheckOutIsNullOrderByCreatedAtDesc(Integer userId);

    boolean existsByUserIdAndDate(Integer userId, LocalDate date);
}
