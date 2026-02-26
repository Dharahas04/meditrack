package com.meditrack.meditrack_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private Integer id;
    private String name;
    private String email;
    private String role;
}
