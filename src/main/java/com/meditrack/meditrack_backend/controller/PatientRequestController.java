package com.meditrack.meditrack_backend.controller;

import com.meditrack.meditrack_backend.dto.PatientRequestActionDTO;
import com.meditrack.meditrack_backend.dto.PatientRequestCreateDTO;
import com.meditrack.meditrack_backend.model.PatientRequest;
import com.meditrack.meditrack_backend.service.PatientRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.meditrack.meditrack_backend.service.UserService;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/patient-requests")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PatientRequestController {

    private final PatientRequestService patientRequestService;
    private final UserService userService;

    // Doctor creates request
    @PostMapping
    public ResponseEntity<PatientRequest> create(@RequestBody PatientRequestCreateDTO dto,
            Authentication authentication) {
        dto.setRequestedByDoctorId(userService.getUserByEmail(authentication.getName()).getId());
        return ResponseEntity.ok(patientRequestService.createRequest(dto));
    }

    // Admin/Receptionist views queue
    @GetMapping
    public ResponseEntity<List<PatientRequest>> getAll(
            @RequestParam(required = false) PatientRequest.RequestStatus status,
            @RequestParam(required = false) Integer doctorId) {

        if (status != null) {
            return ResponseEntity.ok(patientRequestService.getByStatus(status));
        }
        if (doctorId != null) {
            return ResponseEntity.ok(patientRequestService.getByDoctor(doctorId));
        }
        return ResponseEntity.ok(patientRequestService.getAll());
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<PatientRequest> approve(
            @PathVariable Integer id,
            @RequestBody PatientRequestActionDTO dto,
            Authentication authentication) {
        Integer processorUserId = userService.getUserByEmail(authentication.getName()).getId();
        return ResponseEntity.ok(patientRequestService.approve(id, dto, processorUserId));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<PatientRequest> reject(
            @PathVariable Integer id,
            @RequestBody PatientRequestActionDTO dto,
            Authentication authentication) {
        Integer processorUserId = userService.getUserByEmail(authentication.getName()).getId();
        return ResponseEntity.ok(patientRequestService.reject(id, dto, processorUserId));
    }

    @PutMapping("/{id}/registered")
    public ResponseEntity<PatientRequest> markRegistered(
            @PathVariable Integer id,
            Authentication authentication) {
        Integer processorUserId = userService.getUserByEmail(authentication.getName()).getId();
        return ResponseEntity.ok(patientRequestService.markRegistered(id, processorUserId));
    }

}
