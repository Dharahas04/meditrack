package com.meditrack.meditrack_backend.controller;

import com.meditrack.meditrack_backend.dto.PrescriptionCreateDTO;
import com.meditrack.meditrack_backend.model.Prescription;
import com.meditrack.meditrack_backend.service.PrescriptionService;
import com.meditrack.meditrack_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Prescription> create(@RequestBody PrescriptionCreateDTO dto, Authentication authentication) {
        dto.setDoctorId(userService.getUserByEmail(authentication.getName()).getId());
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
    public ResponseEntity<Prescription> stop(@PathVariable Integer id, Authentication authentication) {
        Integer doctorId = userService.getUserByEmail(authentication.getName()).getId();
        return ResponseEntity.ok(prescriptionService.stopPrescription(id, doctorId));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Prescription> complete(@PathVariable Integer id, Authentication authentication) {
        Integer doctorId = userService.getUserByEmail(authentication.getName()).getId();
        return ResponseEntity.ok(prescriptionService.completePrescription(id, doctorId));
    }

}
