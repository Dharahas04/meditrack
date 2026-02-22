package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.model.Attendance;
import com.meditrack.meditrack_backend.model.User;
import com.meditrack.meditrack_backend.repository.AttendanceRepository;
import com.meditrack.meditrack_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    // Mark check in
    public Attendance checkIn(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Attendance attendance = new Attendance();
        attendance.setUser(user);
        attendance.setDate(LocalDate.now());
        attendance.setCheckIn(LocalTime.now());
        attendance.setStatus(Attendance.AttendanceStatus.PRESENT);
        return attendanceRepository.save(attendance);
    }

    // Mark check out
    public Attendance checkOut(Integer userId) {
        Attendance attendance = attendanceRepository
                .findByUserIdAndDate(userId, LocalDate.now())
                .orElseThrow(() -> new RuntimeException("Attendance not found"));
        attendance.setCheckOut(LocalTime.now());
        return attendanceRepository.save(attendance);
    }

    // Get attendance by user
    public List<Attendance> getAttendanceByUser(Integer userId) {
        return attendanceRepository.findByUserId(userId);
    }

    // Get attendance by date
    public List<Attendance> getAttendanceByDate(LocalDate date) {
        return attendanceRepository.findByDate(date);
    }

    // Get all attendance
    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }
}