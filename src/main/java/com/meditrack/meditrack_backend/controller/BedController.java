package com.meditrack.meditrack_backend.controller;

import com.meditrack.meditrack_backend.model.Bed;
import com.meditrack.meditrack_backend.service.BedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beds")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BedController {

    private final BedService bedService;

    // Get all beds
    @GetMapping
    public ResponseEntity<List<Bed>> getAllBeds() {
        return ResponseEntity.ok(bedService.getAllBeds());
    }

    // Get available beds
    @GetMapping("/available")
    public ResponseEntity<List<Bed>> getAvailableBeds() {
        return ResponseEntity.ok(bedService.getAvailableBeds());
    }

    // Get beds by ward
    @GetMapping("/ward/{wardId}")
    public ResponseEntity<List<Bed>> getBedsByWard(
            @PathVariable Integer wardId) {
        return ResponseEntity.ok(bedService.getBedsByWard(wardId));
    }

    // Create bed
    @PostMapping
    public ResponseEntity<Bed> createBed(@RequestBody Bed bed) {
        return ResponseEntity.ok(bedService.createBed(bed));
    }

    // Update bed status
    @PutMapping("/{id}/status")
    public ResponseEntity<Bed> updateBedStatus(
            @PathVariable Integer id,
            @RequestParam Bed.BedStatus status) {
        return ResponseEntity.ok(bedService.updateBedStatus(id, status));
    }
}