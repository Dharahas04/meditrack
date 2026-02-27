package com.meditrack.meditrack_backend.controller;

import com.meditrack.meditrack_backend.model.Appointment;
import com.meditrack.meditrack_backend.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.meditrack.meditrack_backend.model.User;
import com.meditrack.meditrack_backend.service.UserService;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Integer id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Appointment>> getByDoctor(@PathVariable Integer doctorId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctor(doctorId));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<Appointment>> getByDate(@PathVariable String date) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDate(LocalDate.parse(date)));
    }

    // Sprint 1 exact endpoint: POST /api/appointments/book
    @PostMapping("/book")
    public ResponseEntity<Appointment> bookAppointment(@RequestBody Appointment appointment) {
        return ResponseEntity.ok(appointmentService.createAppointment(appointment));
    }

    // Existing endpoint kept
    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
        return ResponseEntity.ok(appointmentService.createAppointment(appointment));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Appointment> updateStatus(
            @PathVariable Integer id,
            @RequestParam Appointment.AppointmentStatus status,
            Authentication authentication) {
        User actor = userService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(appointmentService.updateStatus(id, status, actor.getId(), actor.getRole()));
    }

}
