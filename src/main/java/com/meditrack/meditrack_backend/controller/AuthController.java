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

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
                user.getName(),
                user.getEmail(),
                user.getRole().name()));
    }
}