package com.meditrack.meditrack_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.meditrack")
public class MedtrackBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(MedtrackBackendApplication.class, args);
    }
}