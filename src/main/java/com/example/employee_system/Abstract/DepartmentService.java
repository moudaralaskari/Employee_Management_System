package com.example.employee_system.Abstract;

import com.example.employee_system.Dto.DepartmentCreate;
import com.example.employee_system.Entity.Department;

import java.util.List;
import java.util.UUID;

public interface DepartmentService {
    Department insert(DepartmentCreate departmentCreate);

    Department findById(UUID departmentId);

    List<Department> findAll();

    void delete(UUID departmentId);
}
