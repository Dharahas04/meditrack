package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.model.Bed;
import com.meditrack.meditrack_backend.repository.BedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BedService {

    private final BedRepository bedRepository;

    public List<Bed> getAllBeds() {
        return bedRepository.findAll();
    }

    public List<Bed> getAvailableBeds() {
        return bedRepository.findByStatus(Bed.BedStatus.AVAILABLE);
    }

    public List<Bed> getBedsByWard(Integer wardId) {
        return bedRepository.findByWardId(wardId);
    }

    public Bed updateBedStatus(Integer id, Bed.BedStatus status) {
        Bed bed = bedRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bed not found"));
        bed.setStatus(status);
        return bedRepository.save(bed);
    }

    public Bed createBed(Bed bed) {
        return bedRepository.save(bed);
    }
}