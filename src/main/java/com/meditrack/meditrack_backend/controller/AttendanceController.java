package com.meditrack.meditrack_backend.controller;

import com.meditrack.meditrack_backend.model.Attendance;
import com.meditrack.meditrack_backend.model.User;
import com.meditrack.meditrack_backend.service.AttendanceService;
import com.meditrack.meditrack_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
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
    private final UserService userService;

    private User currentUser(Authentication authentication) {
        return userService.getUserByEmail(authentication.getName());
    }

    private boolean isAdmin(User user) {
        return user.getRole() == User.Role.ADMIN;
    }

    @GetMapping
    public ResponseEntity<List<Attendance>> getAllAttendance(Authentication authentication) {
        User me = currentUser(authentication);
        if (!isAdmin(me)) {
            throw new AccessDeniedException("Only admin can access all attendance");
        }
        return ResponseEntity.ok(attendanceService.getAllAttendance());
    }

    @PostMapping("/mark")
    public ResponseEntity<Attendance> markAttendance(
            @RequestBody Map<String, Object> payload,
            Authentication authentication) {

        User me = currentUser(authentication);

        Object rawUserId = payload.get("userId");
        Integer requestedUserId = rawUserId == null ? null : Integer.valueOf(String.valueOf(rawUserId));
        Integer targetUserId = isAdmin(me) ? requestedUserId : me.getId();

        if (targetUserId == null) {
            throw new RuntimeException("userId is required for admin requests");
        }

        String action = String.valueOf(payload.getOrDefault("action", "CHECKIN")).toUpperCase();
        if ("CHECKOUT".equals(action)) {
            return ResponseEntity.ok(attendanceService.checkOut(targetUserId));
        }
        return ResponseEntity.ok(attendanceService.checkIn(targetUserId));
    }

    @PostMapping("/checkin/{userId}")
    public ResponseEntity<Attendance> checkIn(
            @PathVariable Integer userId,
            Authentication authentication) {

        User me = currentUser(authentication);
        if (!isAdmin(me) && !me.getId().equals(userId)) {
            throw new AccessDeniedException("Cannot check in for another user");
        }
        return ResponseEntity.ok(attendanceService.checkIn(userId));
    }

    @PutMapping("/checkout/{userId}")
    public ResponseEntity<Attendance> checkOut(
            @PathVariable Integer userId,
            Authentication authentication) {

        User me = currentUser(authentication);
        if (!isAdmin(me) && !me.getId().equals(userId)) {
            throw new AccessDeniedException("Cannot check out for another user");
        }
        return ResponseEntity.ok(attendanceService.checkOut(userId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Attendance>> getByUser(
            @PathVariable Integer userId,
            Authentication authentication) {

        User me = currentUser(authentication);
        if (!isAdmin(me) && !me.getId().equals(userId)) {
            throw new AccessDeniedException("Cannot access attendance of other users");
        }
        return ResponseEntity.ok(attendanceService.getAttendanceByUser(userId));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<Attendance>> getByDate(
            @PathVariable String date,
            Authentication authentication) {
        User me = currentUser(authentication);
        if (!isAdmin(me)) {
            throw new AccessDeniedException("Only admin can access attendance by date");
        }
        return ResponseEntity.ok(attendanceService.getAttendanceByDate(LocalDate.parse(date)));
    }

    @GetMapping("/report")
    public ResponseEntity<List<Attendance>> getAttendanceReport(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) String date,
            Authentication authentication) {

        User me = currentUser(authentication);

        if (!isAdmin(me)) {
            if (userId != null && !userId.equals(me.getId())) {
                throw new AccessDeniedException("Cannot access attendance of other users");
            }
            return ResponseEntity.ok(attendanceService.getAttendanceByUser(me.getId()));
        }

        if (userId != null) {
            return ResponseEntity.ok(attendanceService.getAttendanceByUser(userId));
        }
        if (date != null && !date.isBlank()) {
            return ResponseEntity.ok(attendanceService.getAttendanceByDate(LocalDate.parse(date)));
        }
        return ResponseEntity.ok(attendanceService.getAllAttendance());
    }
}
