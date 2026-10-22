package com.example.employee_system.Dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

public record EmployeeCreate(
        @NotNull(message = "first name is required")
        @Size(min = 2, max = 50, message = "minimum is 2 character and maximum 50 character")
        String firstName,
        @NotNull(message = "last name is required")
        @Size(min = 2, max = 50, message = "minimum is 2 character and maximum 50 character")
        String lastName,
        @NotNull
        @Email(message = "invalid email format")
        String email,
        @NotNull
        @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "invalid phone number format")
        String phoneNumber,
        @NotNull
        @PastOrPresent(message = "Hire date can't be feature")
        LocalDate hireDate,
        @NotNull(message = "position name is required")
        @Size(min = 2, max = 50, message = "minimum is 2 character and maximum 50 character")
        String position,
        @NotNull(message = "position name is required")
        UUID departmentId
) {
}
