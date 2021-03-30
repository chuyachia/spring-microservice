package com.chuya.departmentservice.service;

import com.chuya.common.exception.NotFoundException;
import com.chuya.common.model.Department;
import com.chuya.departmentservice.repository.DepartmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public Department getById(Long id) {
        Optional<Department> department = departmentRepository.findById(id);
        return department
                .orElseThrow(() -> new NotFoundException(String.format("Department (id: %s) does not exist", id)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Department saveDepartment(Department department) {
        return departmentRepository.save(department);
    }
}
