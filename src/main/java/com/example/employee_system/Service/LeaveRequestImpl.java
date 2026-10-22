package com.example.employee_system.Service;

import com.example.employee_system.Abstract.EmployeeService;
import com.example.employee_system.Abstract.LeaveRequestService;
import com.example.employee_system.Dto.LeaveRequestCreate;
import com.example.employee_system.Entity.LeaveRequest;
import com.example.employee_system.Repository.LeaveRequestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LeaveRequestImpl implements LeaveRequestService {

    @Autowired
    private LeaveRequestRepo leaveRequestRepo;
    @Autowired
    private EmployeeService employeeService;

    @Override
    @PreAuthorize("@securityUtils.isOwner(#employeeId)")
    public LeaveRequest insert(LeaveRequestCreate leaveRequestCreate, UUID employeeId) {
        LeaveRequest leaveRequest = new LeaveRequest();

        leaveRequest.setReason(leaveRequestCreate.reason());
        leaveRequest.setStartDate(leaveRequestCreate.startDate());
        leaveRequest.setEndDate(leaveRequestCreate.endDate());
        leaveRequest.setStatus("PENDING");
        leaveRequest.setEmployee(employeeService.findById(employeeId));

        leaveRequestRepo.save(leaveRequest);

        return leaveRequest;
    }

    @Override
    @PreAuthorize("@securityUtils.isOwner(#employeeId)")
    public List<LeaveRequest> findAllByEmployeeId(UUID employeeId) {
        return leaveRequestRepo.findByEmployeeId(employeeId);
    }
}
