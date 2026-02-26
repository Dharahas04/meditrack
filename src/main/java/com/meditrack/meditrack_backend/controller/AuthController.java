package com.meditrack.meditrack_backend.controller;

import com.meditrack.meditrack_backend.dto.LoginRequest;
import com.meditrack.meditrack_backend.dto.LoginResponse;
import com.meditrack.meditrack_backend.model.User;
import com.meditrack.meditrack_backend.repository.UserRepository;
import com.meditrack.meditrack_backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.meditrack.meditrack_backend.dto.RegisterRequest;
import com.meditrack.meditrack_backend.model.Department;
import com.meditrack.meditrack_backend.repository.DepartmentRepository;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

        private final AuthenticationManager authenticationManager;
        private final JwtUtil jwtUtil;
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final DepartmentRepository departmentRepository;

        @PostMapping("/register")
        public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
                if (request.getRole() == null) {
                        return ResponseEntity.badRequest().body("Role is required");
                }

                if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
                }

                User.Role role = request.getRole();
                boolean doctor = role == User.Role.DOCTOR;
                boolean shiftRequired = role == User.Role.DOCTOR
                                || role == User.Role.NURSE
                                || role == User.Role.RECEPTIONIST;

                if (doctor && request.getDepartmentId() == null) {
                        return ResponseEntity.badRequest().body("Department is required for doctor");
                }

                if (shiftRequired && request.getShift() == null) {
                        return ResponseEntity.badRequest().body("Shift is required for this role");
                }

                Department department;
                if (doctor) {
                        department = departmentRepository.findById(request.getDepartmentId())
                                        .orElseThrow(() -> new RuntimeException("Department not found"));
                } else {
                        department = departmentRepository.findByNameIgnoreCase("GENERAL_SUPPORT")
                                        .orElseThrow(() -> new RuntimeException(
                                                        "GENERAL_SUPPORT department not found"));
                }

                User user = new User();
                user.setName(request.getName());
                user.setEmail(request.getEmail());
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                user.setRole(role);
                user.setPhone(request.getPhone());
                user.setDepartment(department);
                user.setShift(shiftRequired ? request.getShift() : null);

                userRepository.save(user);
                return ResponseEntity.ok("User registered successfully");
        }

        @PostMapping("/login")
        public ResponseEntity<LoginResponse> login(
                        @RequestBody LoginRequest request) {

                // Authenticate user
                // Authenticate user - throws exception if credentials wrong
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getEmail(),
                                                request.getPassword()));

                // Get user details
                User user = userRepository.findByEmail(request.getEmail())
                                .orElseThrow(() -> new RuntimeException("User not found"));

                // Generate JWT token
                String token = jwtUtil.generateToken(
                                user.getEmail(),
                                user.getRole().name());

                return ResponseEntity.ok(new LoginResponse(
                                token,
                                user.getId(),
                                user.getName(),
                                user.getEmail(),
                                user.getRole().name()));

        }
}