package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.model.Attendance;
import com.meditrack.meditrack_backend.model.User;
import com.meditrack.meditrack_backend.repository.AttendanceRepository;
import com.meditrack.meditrack_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private static final int CHECKIN_GRACE_MINUTES = 15;
    private static final long HALF_DAY_MINUTES = 4 * 60; // 4 hours

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    public Attendance checkIn(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDate today = LocalDate.now();

        // Do not allow new check-in if old shift is still open.
        if (attendanceRepository.findTopByUserIdAndCheckOutIsNullOrderByCreatedAtDesc(userId).isPresent()) {
            throw new RuntimeException("Pending checkout exists. Complete checkout first.");
        }

        // One check-in per user per date.
        if (attendanceRepository.existsByUserIdAndDate(userId, today)) {
            throw new RuntimeException("Already checked in for today");
        }

        LocalTime now = LocalTime.now();

        Attendance attendance = new Attendance();
        attendance.setUser(user);
        attendance.setDate(today);
        attendance.setCheckIn(now);

        if (isLate(user, now)) {
            attendance.setStatus(Attendance.AttendanceStatus.LATE);
        } else {
            attendance.setStatus(Attendance.AttendanceStatus.PRESENT);
        }

        return attendanceRepository.save(attendance);
    }

    public Attendance checkOut(Integer userId) {
        Attendance attendance = attendanceRepository
                .findTopByUserIdAndCheckOutIsNullOrderByCreatedAtDesc(userId)
                .orElseThrow(() -> new RuntimeException("No active check-in found"));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime checkInDateTime = LocalDateTime.of(attendance.getDate(), attendance.getCheckIn());

        long workedMinutes = Duration.between(checkInDateTime, now).toMinutes();
        if (workedMinutes < 0)
            workedMinutes = 0;

        attendance.setCheckOut(now.toLocalTime());

        // If worked hours are too low, mark half day.
        if (workedMinutes < HALF_DAY_MINUTES) {
            attendance.setStatus(Attendance.AttendanceStatus.HALF_DAY);
        } else if (attendance.getStatus() != Attendance.AttendanceStatus.LATE) {
            attendance.setStatus(Attendance.AttendanceStatus.PRESENT);
        }

        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getAttendanceByUser(Integer userId) {
        return attendanceRepository.findByUserId(userId);
    }

    public List<Attendance> getAttendanceByDate(LocalDate date) {
        return attendanceRepository.findByDate(date);
    }

    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    private boolean isLate(User user, LocalTime checkInTime) {
        if (user.getShift() == null)
            return false;

        LocalTime shiftStart;
        switch (user.getShift()) {
            case MORNING:
                shiftStart = LocalTime.of(9, 0);
                break;
            case AFTERNOON:
                shiftStart = LocalTime.of(14, 0);
                break;
            case NIGHT:
                shiftStart = LocalTime.of(21, 0);
                break;
            default:
                return false;
        }

        return checkInTime.isAfter(shiftStart.plusMinutes(CHECKIN_GRACE_MINUTES));
    }
}
