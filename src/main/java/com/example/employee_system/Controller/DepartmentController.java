package com.example.employee_system.Controller;

import com.example.employee_system.Abstract.DepartmentService;
import com.example.employee_system.Dto.DepartmentCreate;
import com.example.employee_system.Entity.Department;
import com.example.employee_system.Shared.GlobalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/{departmentId}")
    public ResponseEntity<GlobalResponse<Department>> findById(@PathVariable UUID departmentId) {
        return new ResponseEntity<>(new GlobalResponse<>(departmentService.findById(departmentId)), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<GlobalResponse<List<Department>>> findAll() {
        return new ResponseEntity<>(new GlobalResponse<>(departmentService.findAll()), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<GlobalResponse<Department>> insert(@RequestBody DepartmentCreate departmentCreate) {
        return new ResponseEntity<>(new GlobalResponse<>(departmentService.insert(departmentCreate)), HttpStatus.CREATED);
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<GlobalResponse<Void>> delete(@PathVariable UUID employeeId) {
        departmentService.delete(employeeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
