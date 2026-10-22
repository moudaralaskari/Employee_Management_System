package com.example.employee_system.Abstract;

import com.example.employee_system.Dto.EmployeeCreate;
import com.example.employee_system.Dto.EmployeeUpdate;
import com.example.employee_system.Entity.Employee;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface EmployeeService {
    Employee findById(UUID employeeId);

    Page<Employee> findAll(int page, int size);

    void deleteOne(UUID employeeId);

    Employee update(EmployeeUpdate employeeUpdate, UUID employeeId);

    Employee insert(EmployeeCreate employee);

}
