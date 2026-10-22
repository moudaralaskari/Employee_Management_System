package com.example.employee_system.Repository;

import com.example.employee_system.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, UUID> {
    Optional<Employee> findOneByAccountCreationToken(String token);

}
