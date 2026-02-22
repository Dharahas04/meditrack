package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.model.Department;
import com.meditrack.meditrack_backend.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(Integer id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
    }

    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public Department updateDepartment(Integer id, Department department) {
        Department existing = getDepartmentById(id);
        existing.setName(department.getName());
        existing.setFloor(department.getFloor());
        return departmentRepository.save(existing);
    }

    public void deleteDepartment(Integer id) {
        departmentRepository.deleteById(id);
    }
}