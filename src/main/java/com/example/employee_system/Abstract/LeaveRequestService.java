package com.example.employee_system.Abstract;

import com.example.employee_system.Dto.LeaveRequestCreate;
import com.example.employee_system.Entity.LeaveRequest;

import java.util.List;
import java.util.UUID;

public interface LeaveRequestService {
    LeaveRequest insert(LeaveRequestCreate leaveRequest, UUID employeeId);

    List<LeaveRequest> findAllByEmployeeId(UUID employeeId);


}
