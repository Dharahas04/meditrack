package com.meditrack.meditrack_backend.controller;

import com.meditrack.meditrack_backend.dto.NurseTaskCreateDTO;
import com.meditrack.meditrack_backend.model.NurseTask;
import com.meditrack.meditrack_backend.service.NurseTaskService;
import com.meditrack.meditrack_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/nurse-tasks")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NurseTaskController {

    private final NurseTaskService nurseTaskService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<NurseTask> create(@RequestBody NurseTaskCreateDTO dto, Authentication authentication) {
        dto.setDoctorId(userService.getUserByEmail(authentication.getName()).getId());
        return ResponseEntity.ok(nurseTaskService.createTask(dto));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<NurseTask>> getByPatient(@PathVariable Integer patientId) {
        return ResponseEntity.ok(nurseTaskService.getByPatient(patientId));
    }

    @GetMapping("/nurse/{nurseId}")
    public ResponseEntity<List<NurseTask>> getByNurse(@PathVariable Integer nurseId) {
        return ResponseEntity.ok(nurseTaskService.getByNurse(nurseId));
    }

    @GetMapping("/nurse/{nurseId}/pending")
    public ResponseEntity<List<NurseTask>> getPendingByNurse(@PathVariable Integer nurseId) {
        return ResponseEntity.ok(nurseTaskService.getPendingByNurse(nurseId));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<NurseTask> complete(
            @PathVariable Integer id,
            Authentication authentication,
            @RequestBody(required = false) Map<String, String> body) {
        Integer nurseId = userService.getUserByEmail(authentication.getName()).getId();
        String notes = body != null ? body.getOrDefault("notes", "") : "";
        return ResponseEntity.ok(nurseTaskService.markCompleted(id, nurseId, notes));
    }

    @PutMapping("/{id}/missed")
    public ResponseEntity<NurseTask> missed(
            @PathVariable Integer id,
            Authentication authentication,
            @RequestBody(required = false) Map<String, String> body) {
        Integer nurseId = userService.getUserByEmail(authentication.getName()).getId();
        String notes = body != null ? body.getOrDefault("notes", "") : "";
        return ResponseEntity.ok(nurseTaskService.markMissed(id, nurseId, notes));
    }
}
