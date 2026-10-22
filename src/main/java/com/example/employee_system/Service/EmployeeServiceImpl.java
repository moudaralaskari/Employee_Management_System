package com.example.employee_system.Service;

import com.example.employee_system.Abstract.DepartmentService;
import com.example.employee_system.Abstract.EmployeeService;
import com.example.employee_system.Dto.EmployeeCreate;
import com.example.employee_system.Dto.EmployeeUpdate;
import com.example.employee_system.Entity.Employee;
import com.example.employee_system.Repository.EmployeeRepo;
import com.example.employee_system.Shared.CustomResponseException;
import com.example.employee_system.Utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepo employeeRepo;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private EmailService emailService;

    @Override
    @PreAuthorize("@securityUtils.isOwner(#employeeId)")
    public Employee findById(UUID employeeId) {
        Employee employee = employeeRepo.findById(employeeId).
                orElseThrow(() -> CustomResponseException.ResourceNotFound("Employee with id " + employeeId + " not found"));
        return employee;
    }

    @Override
    public Page<Employee> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return employeeRepo.findAll(pageable);
    }

    @Override
    public void deleteOne(UUID employeeId) {
        Employee employee = findById(employeeId);
        employeeRepo.deleteById(employeeId);

    }

    @PreAuthorize("@securityUtils.isOwner(#employeeId)")
    public Employee update(EmployeeUpdate employeeUpdate, UUID employeeId) {
        Employee ExistingEmployee = findById(employeeId);

        ExistingEmployee.setPosition(employeeUpdate.position());
        ExistingEmployee.setFirstName(employeeUpdate.firstName());
        ExistingEmployee.setLastName(employeeUpdate.lastName());
        ExistingEmployee.setPhoneNumber(employeeUpdate.phoneNumber());

        employeeRepo.save(ExistingEmployee);
        return ExistingEmployee;
    }

    @Transactional
    public Employee insert(EmployeeCreate employeeCreate) {
        Employee employee = new Employee();

        String token = UUID.randomUUID().toString();

        employee.setPhoneNumber(employeeCreate.phoneNumber());
        employee.setEmail(employeeCreate.email());
        employee.setPosition(employeeCreate.position());
        employee.setHireDate(employeeCreate.hireDate());
        employee.setFirstName(employeeCreate.firstName());
        employee.setLastName(employeeCreate.lastName());

        employee.setDepartment(
                departmentService.findById(employeeCreate.departmentId())
        );

        employee.setVerified(false);
        employee.setAccountCreationToken(token);

        employeeRepo.save(employee);

        emailService.sendAccountCreationEmail(
                employee.getEmail(), token
        );

        return employee;
    }


}
