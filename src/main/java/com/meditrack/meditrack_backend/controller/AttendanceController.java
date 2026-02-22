package com.meditrack.meditrack_backend.controller;

import com.meditrack.meditrack_backend.model.Attendance;
import com.meditrack.meditrack_backend.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AttendanceController {

    private final AttendanceService attendanceService;

    // Get all attendance
    @GetMapping
    public ResponseEntity<List<Attendance>> getAllAttendance() {
        return ResponseEntity.ok(attendanceService.getAllAttendance());
    }

    // Check in
    @PostMapping("/checkin/{userId}")
    public ResponseEntity<Attendance> checkIn(
            @PathVariable Integer userId) {
        return ResponseEntity.ok(attendanceService.checkIn(userId));
    }

    // Check out
    @PutMapping("/checkout/{userId}")
    public ResponseEntity<Attendance> checkOut(
            @PathVariable Integer userId) {
        return ResponseEntity.ok(attendanceService.checkOut(userId));
    }

    // Get attendance by user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Attendance>> getByUser(
            @PathVariable Integer userId) {
        return ResponseEntity.ok(
                attendanceService.getAttendanceByUser(userId));
    }

    // Get attendance by date
    @GetMapping("/date/{date}")
    public ResponseEntity<List<Attendance>> getByDate(
            @PathVariable String date) {
        return ResponseEntity.ok(
                attendanceService.getAttendanceByDate(
                        LocalDate.parse(date)));
    }
}