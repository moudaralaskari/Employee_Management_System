package com.example.employee_system.Dto;

import java.util.List;

public record PaginatedResponse<T>(
        List<T> content,
        int currentPage,
        int totalPages,
        long totalItems,
        boolean hasNext,
        boolean hasPrevious,
        String nextPageUrl,
        String previousPageUrl


) {
}
