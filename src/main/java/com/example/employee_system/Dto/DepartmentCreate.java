package com.example.employee_system.Dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DepartmentCreate(
        @NotNull(message = "department name is required")
        @Size(min = 2, max = 50, message = "minimum is 2 character and maximum 50 character")
        String name
) {
}
