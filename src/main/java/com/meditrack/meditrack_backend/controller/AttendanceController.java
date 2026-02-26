package com.meditrack.meditrack_backend.controller;

import com.meditrack.meditrack_backend.model.Attendance;
import com.meditrack.meditrack_backend.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping
    public ResponseEntity<List<Attendance>> getAllAttendance() {
        return ResponseEntity.ok(attendanceService.getAllAttendance());
    }

    // Sprint 1 exact endpoint: POST /api/attendance/mark
    @PostMapping("/mark")
    public ResponseEntity<Attendance> markAttendance(
            @RequestBody Map<String, Object> payload) {

        Object userIdRaw = payload.get("userId");
        if (userIdRaw == null) {
            throw new RuntimeException("userId is required");
        }

        Integer userId = Integer.valueOf(String.valueOf(userIdRaw));
        String action = String.valueOf(payload.getOrDefault("action", "CHECKIN")).toUpperCase();

        if ("CHECKOUT".equals(action)) {
            return ResponseEntity.ok(attendanceService.checkOut(userId));
        }

        return ResponseEntity.ok(attendanceService.checkIn(userId));
    }

    @PostMapping("/checkin/{userId}")
    public ResponseEntity<Attendance> checkIn(@PathVariable Integer userId) {
        return ResponseEntity.ok(attendanceService.checkIn(userId));
    }

    @PutMapping("/checkout/{userId}")
    public ResponseEntity<Attendance> checkOut(@PathVariable Integer userId) {
        return ResponseEntity.ok(attendanceService.checkOut(userId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Attendance>> getByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(attendanceService.getAttendanceByUser(userId));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<Attendance>> getByDate(@PathVariable String date) {
        return ResponseEntity.ok(attendanceService.getAttendanceByDate(LocalDate.parse(date)));
    }

    // Sprint 1 exact endpoint: GET /api/attendance/report
    @GetMapping("/report")
    public ResponseEntity<List<Attendance>> getAttendanceReport(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) String date) {

        if (userId != null) {
            return ResponseEntity.ok(attendanceService.getAttendanceByUser(userId));
        }

        if (date != null && !date.isBlank()) {
            return ResponseEntity.ok(attendanceService.getAttendanceByDate(LocalDate.parse(date)));
        }

        return ResponseEntity.ok(attendanceService.getAllAttendance());
    }
}
