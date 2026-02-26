package com.meditrack.meditrack_backend.dto;

import com.meditrack.meditrack_backend.model.User;
import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private User.Role role;
    private String phone;
    private Integer departmentId;
    private User.Shift shift;
}
