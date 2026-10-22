package com.example.employee_system.Controller;

import com.example.employee_system.Abstract.EmployeeService;
import com.example.employee_system.Abstract.LeaveRequestService;
import com.example.employee_system.Dto.EmployeeCreate;
import com.example.employee_system.Dto.EmployeeUpdate;
import com.example.employee_system.Dto.LeaveRequestCreate;
import com.example.employee_system.Dto.PaginatedResponse;
import com.example.employee_system.Entity.Employee;
import com.example.employee_system.Entity.LeaveRequest;
import com.example.employee_system.Shared.GlobalResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/employee")
public class EmployeeController {


    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private LeaveRequestService leaveRequestService;

    @GetMapping()
    public ResponseEntity<GlobalResponse<PaginatedResponse<Employee>>>
    findAll(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "3") int size,
            HttpServletRequest request) {
        int zeroBasedPage = page - 1;
        Page<Employee> employees = employeeService.findAll(zeroBasedPage, size);
        String baseUrl = request.getRequestURL().toString();
        String nextUrl = employees.hasNext() ? String.format("%s?page=%d&size=%d", baseUrl, page + 1, size) : null;
        String previousUrl = employees.hasPrevious() ? String.format("%s?page=%d&size=%d", baseUrl, page - 1, size) : null;
        PaginatedResponse<Employee> paginatedResponse = new PaginatedResponse<Employee>(
                employees.getContent(),
                employees.getNumber(),
                employees.getTotalPages(),
                employees.getTotalElements(),
                employees.hasNext(),
                employees.hasPrevious(),
                nextUrl,
                previousUrl
        );
        return new ResponseEntity<>(new GlobalResponse<>(paginatedResponse), HttpStatus.OK);
    }


    @GetMapping("/{employeeId}")
    public ResponseEntity<GlobalResponse<Employee>> findById(@PathVariable UUID employeeId) {
        Employee employee = employeeService.findById(employeeId);
        return new ResponseEntity<>(new GlobalResponse<>(employee), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<GlobalResponse<Employee>> insert(@RequestBody @Valid EmployeeCreate employeeCreate) {

        return new ResponseEntity<>(new GlobalResponse<>(employeeService.insert(employeeCreate)), HttpStatus.CREATED);
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteOne(@PathVariable UUID employeeId) {
        employeeService.deleteOne(employeeId);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<GlobalResponse<Employee>> update(@RequestBody @Valid EmployeeUpdate employeeUpdate, @PathVariable UUID employeeId) {

        return new ResponseEntity<>(new GlobalResponse<>(employeeService.update(employeeUpdate, employeeId)), HttpStatus.OK);
    }

    @PostMapping("/{employeeId}/leave-request")
    public ResponseEntity<GlobalResponse<LeaveRequest>> leaveRequestCreate(@PathVariable UUID employeeId, @RequestBody LeaveRequestCreate leaveRequestCreate) {

        return new ResponseEntity<>(new GlobalResponse<>(leaveRequestService.insert(leaveRequestCreate, employeeId)), HttpStatus.CREATED);
    }

    @GetMapping("/{employeeId}/leave-request")
    public ResponseEntity<GlobalResponse<List<LeaveRequest>>> findAllByEmployeeId(@PathVariable UUID employeeId) {
        return new ResponseEntity<>(new GlobalResponse<>(leaveRequestService.findAllByEmployeeId(employeeId)), HttpStatus.OK);
    }

}
