package com.bar.service;

import com.bar.model.Employee;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {
    private List<Employee> employees;
    private long nextId = 4;

    public EmployeeService() {
        this.employees = new ArrayList<>();
        initializeEmployees();
    }

    private void initializeEmployees() {
        employees.add(new Employee(1L, "Marco Rossi", "mattina"));
        employees.add(new Employee(2L, "Luigi Bianchi", "pomeriggio"));
        employees.add(new Employee(3L, "Anna Verdi", "sera"));
    }

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);
    }

    public Employee addEmployee(Employee employee) {
        employee.setId(nextId++);
        employees.add(employee);
        return employee;
    }

    public void deleteEmployee(Long id) {
        employees.removeIf(emp -> emp.getId().equals(id));
    }
}
