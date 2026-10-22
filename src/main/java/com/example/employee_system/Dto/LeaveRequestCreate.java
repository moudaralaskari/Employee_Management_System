package com.example.employee_system.Dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record LeaveRequestCreate(
        @FutureOrPresent(message = "start date should be now or in the feature")
        @NotNull(message = "start date is required")
        LocalDate startDate,

        @FutureOrPresent(message = "end date should be now or in the feature")
        @NotNull(message = "end date is required")
        LocalDate endDate,

        @NotNull(message = "reason is required")
        @Size(min = 2, max = 100, message = "min is 2 characters and max 100 characters")
        String reason
) {
}
