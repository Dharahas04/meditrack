package com.meditrack.meditrack_backend.controller;

import com.meditrack.meditrack_backend.model.Appointment;
import com.meditrack.meditrack_backend.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AppointmentController {

    private final AppointmentService appointmentService;

    // Get all appointments
    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    // Get appointment by ID
    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(
            @PathVariable Integer id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    // Get appointments by doctor
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Appointment>> getByDoctor(
            @PathVariable Integer doctorId) {
        return ResponseEntity.ok(
                appointmentService.getAppointmentsByDoctor(doctorId));
    }

    // Get appointments by date
    @GetMapping("/date/{date}")
    public ResponseEntity<List<Appointment>> getByDate(
            @PathVariable String date) {
        return ResponseEntity.ok(
                appointmentService.getAppointmentsByDate(
                        LocalDate.parse(date)));
    }

    // Create appointment
    @PostMapping
    public ResponseEntity<Appointment> createAppointment(
            @RequestBody Appointment appointment) {
        return ResponseEntity.ok(
                appointmentService.createAppointment(appointment));
    }

    // Update appointment status
    @PutMapping("/{id}/status")
    public ResponseEntity<Appointment> updateStatus(
            @PathVariable Integer id,
            @RequestParam Appointment.AppointmentStatus status) {
        return ResponseEntity.ok(
                appointmentService.updateStatus(id, status));
    }
}