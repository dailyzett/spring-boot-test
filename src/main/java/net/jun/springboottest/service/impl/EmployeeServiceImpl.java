package net.jun.springboottest.service.impl;

import lombok.RequiredArgsConstructor;
import net.jun.springboottest.exception.ResourceNotFoundException;
import net.jun.springboottest.model.Employee;
import net.jun.springboottest.repository.EmployeeRepository;
import net.jun.springboottest.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Employee saveEmployee(Employee employee) {

        employeeRepository.findByEmail(employee.getEmail())
                .ifPresent(e -> {
                    throw new ResourceNotFoundException("Employee already exists with email: " + employee.getEmail());
                });

        return employeeRepository.save(employee);
    }
}
