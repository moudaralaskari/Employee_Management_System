package com.example.employee_system;

import com.example.employee_system.Abstract.EmployeeService;
import com.example.employee_system.Abstract.LeaveRequestService;
import com.example.employee_system.Config.JwtHelper;
import com.example.employee_system.Controller.EmployeeController;
import com.example.employee_system.Entity.Department;
import com.example.employee_system.Entity.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private JwtHelper jwtHelper;
    @MockitoBean
    private EmployeeService employeeService;
    @MockitoBean
    private LeaveRequestService leaveRequestService;
    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void shouldReturnAllEmployee() throws Exception {
        //  Arrange
        List<Employee> employees = List.of(
                new Employee(
                        UUID.randomUUID(), "John", "Doe",
                        "john@gmail.com", "0123456789",
                        LocalDate.now(), "Developer",
                        false, null,
                        new Department(UUID.randomUUID(), "IT"))
        );
        Page<Employee> employeePage = new PageImpl<>(employees);
        when(employeeService.findAll(0, 3)).thenReturn(employeePage);
        mockMvc.perform(get("/employee").with(user("admin").roles("ADMIN")))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].firstName").value("John"));
        ;
    }
}
