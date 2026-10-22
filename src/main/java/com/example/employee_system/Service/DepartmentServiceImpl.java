package com.example.employee_system.Service;

import com.example.employee_system.Abstract.DepartmentService;
import com.example.employee_system.Dto.DepartmentCreate;
import com.example.employee_system.Entity.Department;
import com.example.employee_system.Repository.DepartmentRepo;
import com.example.employee_system.Shared.CustomResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepo departmentRepo;

    @Override
    public Department insert(DepartmentCreate departmentCreate) {
        Department department = new Department();
        department.setName(departmentCreate.name());
        departmentRepo.save(department);
        return department;
    }

    @Override
    public Department findById(UUID departmentId) {
        return departmentRepo.findById(departmentId)
                .orElseThrow(() -> CustomResponseException.ResourceNotFound("department with id " + departmentId + " not found"));
    }

    @Override
    public List<Department> findAll() {
        return departmentRepo.findAll();
    }

    @Override
    public void delete(UUID departmentId) {
        findById(departmentId);
        departmentRepo.deleteById(departmentId);
    }
}
