package com.meditrack.meditrack_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "wards")
public class Ward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    private String floor;

    @Column(name = "total_beds")
    private Integer totalBeds = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}