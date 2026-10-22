package com.example.employee_system;

import com.example.employee_system.Abstract.DepartmentService;
import com.example.employee_system.Dto.EmployeeCreate;
import com.example.employee_system.Entity.Department;
import com.example.employee_system.Entity.Employee;
import com.example.employee_system.Repository.EmployeeRepo;
import com.example.employee_system.Service.EmailService;
import com.example.employee_system.Service.EmployeeServiceImpl;
import com.example.employee_system.Shared.CustomResponseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
    @Mock
    private DepartmentService departmentService;

    @Mock
    private EmployeeRepo employeeRepo;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Department testDepartment;
    private UUID departmentId;
    private EmployeeCreate employeeCreate;

    @BeforeEach
    void setUp() {
        departmentId = UUID.randomUUID();
        testDepartment = new Department(departmentId, "IT");
        employeeCreate = new EmployeeCreate(
                "John",
                "Doe",
                "john@example.com",
                "1234567890",
                LocalDate.now(),
                "Developer",
                departmentId
        );
    }

    @Test
    @DisplayName("createOne should create employee successfully")
    void createOne_shouldCreateEmployeeSuccessfully() {
        // ARRANGE

        when(departmentService.findById(departmentId))
                .thenReturn(testDepartment);
        when(employeeRepo.save(any(Employee.class)))
                .thenAnswer(i -> i.getArgument(0));

        doNothing().when(emailService)
                .sendAccountCreationEmail(
                        any(String.class), any(String.class));

        // ACT
        Employee result = employeeService.insert(employeeCreate);

        // ASSERT
        assertEquals("John", result.getFirstName());

        // Verify
        verify(emailService, times(1))
                .sendAccountCreationEmail(eq("john@example.com"), any(String.class));
    }

    @Test
    @DisplayName("createOne should throw exception when department not found")
    void createOne_shouldThrowExceptionWhenDepNotFound() {
        when(departmentService.findById(departmentId)).thenThrow(
                CustomResponseException.ResourceNotFound(
                        "department with id " + departmentId + " not found"
                )
        );

        CustomResponseException exception = assertThrows(
                CustomResponseException.class,
                () -> employeeService.insert(employeeCreate)
        );

        assertTrue(exception.getMessage().contains("department with id " + departmentId + " not found"));

        verify(emailService, never())
                .sendAccountCreationEmail(any(String.class), any(String.class));
        verify(employeeRepo, never())
                .save(any(Employee.class));
    }
}