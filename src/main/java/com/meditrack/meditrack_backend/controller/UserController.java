package com.meditrack.meditrack_backend.controller;

import com.meditrack.meditrack_backend.dto.UserSummaryResponse;
import com.meditrack.meditrack_backend.model.User;
import com.meditrack.meditrack_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserSummaryResponse>> getUsers(
            @RequestParam(required = false) String role) {

        List<User> users;
        if (role == null || role.isBlank()) {
            users = userService.getAllUsers();
        } else {
            User.Role parsedRole = User.Role.valueOf(role.toUpperCase());
            users = userService.getUsersByRole(parsedRole);
        }

        return ResponseEntity.ok(
                users.stream().map(UserSummaryResponse::from).toList());
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<UserSummaryResponse>> getDoctors() {
        List<User> doctors = userService.getUsersByRole(User.Role.DOCTOR);
        return ResponseEntity.ok(
                doctors.stream().map(UserSummaryResponse::from).toList());
    }
}
