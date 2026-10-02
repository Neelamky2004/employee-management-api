package com.neelam.employee.service;

import com.neelam.employee.exception.DuplicateEmailException;
import com.neelam.employee.exception.ResourceNotFoundException;
import com.neelam.employee.model.Employee;
import com.neelam.employee.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public List<Employee> getAll() {
        return repository.findAll();
    }

    public Employee getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));
    }

    public List<Employee> getByDepartment(String department) {
        return repository.findByDepartmentIgnoreCase(department);
    }

    public Employee create(Employee employee) {
        if (repository.existsByEmail(employee.getEmail())) {
            throw new DuplicateEmailException("Email already exists: " + employee.getEmail());
        }
        employee.setId(null);
        return repository.save(employee);
    }

    public Employee update(Long id, Employee updated) {
        Employee existing = getById(id);
        if (!existing.getEmail().equals(updated.getEmail()) && repository.existsByEmail(updated.getEmail())) {
            throw new DuplicateEmailException("Email already exists: " + updated.getEmail());
        }
        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        existing.setDepartment(updated.getDepartment());
        existing.setDesignation(updated.getDesignation());
        existing.setJoiningDate(updated.getJoiningDate());
        return repository.save(existing);
    }

    public void delete(Long id) {
        Employee existing = getById(id);
        repository.delete(existing);
    }
}
