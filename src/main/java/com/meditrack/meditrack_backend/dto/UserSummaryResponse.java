package com.meditrack.meditrack_backend.dto;

import com.meditrack.meditrack_backend.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserSummaryResponse {
    private Integer id;
    private String name;
    private String email;
    private String role;
    private String department;
    private String shift;
    private String phone;
    private Boolean isActive;

    public static UserSummaryResponse from(User user) {
        return new UserSummaryResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole() != null ? user.getRole().name() : null,
                user.getDepartment() != null ? user.getDepartment().getName() : null,
                user.getShift() != null ? user.getShift().name() : null,
                user.getPhone(),
                user.getIsActive());
    }
}
