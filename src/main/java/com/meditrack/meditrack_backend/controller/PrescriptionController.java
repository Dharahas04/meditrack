package com.meditrack.meditrack_backend.controller;

import com.meditrack.meditrack_backend.dto.PrescriptionCreateDTO;
import com.meditrack.meditrack_backend.model.Prescription;
import com.meditrack.meditrack_backend.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @PostMapping
    public ResponseEntity<Prescription> create(@RequestBody PrescriptionCreateDTO dto) {
        return ResponseEntity.ok(prescriptionService.createPrescription(dto));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Prescription>> getByPatient(@PathVariable Integer patientId) {
        return ResponseEntity.ok(prescriptionService.getByPatient(patientId));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Prescription>> getByDoctor(@PathVariable Integer doctorId) {
        return ResponseEntity.ok(prescriptionService.getByDoctor(doctorId));
    }

    @GetMapping("/nurse/{nurseId}")
    public ResponseEntity<List<Prescription>> getByNurse(@PathVariable Integer nurseId) {
        return ResponseEntity.ok(prescriptionService.getByNurse(nurseId));
    }

    @PutMapping("/{id}/stop")
    public ResponseEntity<Prescription> stop(@PathVariable Integer id) {
        return ResponseEntity.ok(prescriptionService.stopPrescription(id));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Prescription> complete(@PathVariable Integer id) {
        return ResponseEntity.ok(prescriptionService.completePrescription(id));
    }
}
