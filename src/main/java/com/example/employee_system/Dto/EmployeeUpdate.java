package com.example.employee_system.Dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EmployeeUpdate(
        @NotNull(message = "first name is required")
        @Size(min = 2, max = 50, message = "minimum is 2 character and maximum 50 character")
        String firstName,
        @NotNull(message = "last name is required")
        @Size(min = 2, max = 50, message = "minimum is 2 character and maximum 50 character")
        String lastName,
        @NotNull
        @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "invalid phone number format")
        String phoneNumber,
        @NotNull(message = "position name is required")
        @Size(min = 2, max = 50, message = "minimum is 2 character and maximum 50 character")
        String position
) {
}
