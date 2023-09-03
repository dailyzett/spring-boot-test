package net.jun.springboottest.service;

import net.jun.springboottest.model.Employee;

import java.util.List;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);

    List<Employee> getAllEmployees();

    void updateEmployee(Employee employee);

    void deleteEmployee(Long id);
}
