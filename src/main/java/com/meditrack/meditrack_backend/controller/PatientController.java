package com.meditrack.meditrack_backend.controller;

import com.meditrack.meditrack_backend.model.Patient;
import com.meditrack.meditrack_backend.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PatientController {

    private final PatientService patientService;

    // Get all patients
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    // Get patient by ID
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Integer id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    // Get patients by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Patient>> getByStatus(
            @PathVariable Patient.PatientStatus status) {
        return ResponseEntity.ok(patientService.getPatientsByStatus(status));
    }

    // Get patients by doctor
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Patient>> getByDoctor(
            @PathVariable Integer doctorId) {
        return ResponseEntity.ok(patientService.getPatientsByDoctor(doctorId));
    }

    // Create patient
    @PostMapping
    public ResponseEntity<Patient> createPatient(
            @RequestBody Patient patient) {
        return ResponseEntity.ok(patientService.createPatient(patient));
    }

    // Update patient
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(
            @PathVariable Integer id,
            @RequestBody Patient patient) {
        return ResponseEntity.ok(patientService.updatePatient(id, patient));
    }

    // Discharge patient
    @PutMapping("/{id}/discharge")
    public ResponseEntity<String> dischargePatient(
            @PathVariable Integer id) {
        patientService.dischargePatient(id);
        return ResponseEntity.ok("Patient discharged successfully");
    }
}