package com.neelam.employee;

import com.neelam.employee.exception.DuplicateEmailException;
import com.neelam.employee.exception.ResourceNotFoundException;
import com.neelam.employee.model.Employee;
import com.neelam.employee.repository.EmployeeRepository;
import com.neelam.employee.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository repository;

    @InjectMocks
    private EmployeeService service;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee("Rahul Sharma", "rahul@test.com", "IT", "Software Engineer", LocalDate.of(2025, 7, 1));
        employee.setId(1L);
    }

    @Test
    void createSavesNewEmployee() {
        when(repository.existsByEmail("rahul@test.com")).thenReturn(false);
        when(repository.save(any(Employee.class))).thenReturn(employee);

        Employee saved = service.create(employee);

        assertEquals("Rahul Sharma", saved.getName());
        verify(repository).save(employee);
    }

    @Test
    void createFailsWhenEmailExists() {
        when(repository.existsByEmail("rahul@test.com")).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> service.create(employee));
        verify(repository, never()).save(any());
    }

    @Test
    void getByIdThrowsWhenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }

    @Test
    void updateChangesFields() {
        Employee changes = new Employee("Rahul Sharma", "rahul@test.com", "HR", "HR Executive", LocalDate.of(2025, 7, 1));
        when(repository.findById(1L)).thenReturn(Optional.of(employee));
        when(repository.save(any(Employee.class))).thenAnswer(inv -> inv.getArgument(0));

        Employee result = service.update(1L, changes);

        assertEquals("HR", result.getDepartment());
        assertEquals("HR Executive", result.getDesignation());
    }

    @Test
    void getByDepartmentReturnsMatchingEmployees() {
        when(repository.findByDepartmentIgnoreCase("it")).thenReturn(List.of(employee));

        assertEquals(1, service.getByDepartment("it").size());
    }

    @Test
    void deleteRemovesEmployee() {
        when(repository.findById(1L)).thenReturn(Optional.of(employee));

        service.delete(1L);

        verify(repository).delete(employee);
    }
}
