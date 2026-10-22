package com.example.employee_system.Dto;

public record ResetPasswordRequest(
        String token,
        String password
) {
}
