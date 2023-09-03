package net.jun.springboottest.service.impl;

import net.jun.springboottest.exception.ResourceNotFoundException;
import net.jun.springboottest.model.Employee;
import net.jun.springboottest.repository.EmployeeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("test@gmail.com")
                .build();
    }

    @Test
    void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);

        //when
        var savedEmployee = employeeService.saveEmployee(employee);

        //then
        assertThat(savedEmployee).isNotNull();
    }

    @Test
    void givenExistingEmail_whenSaveEmployee_thenThrowsException() {
        //given
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

        //when
        Assertions.assertThatThrownBy(() -> employeeService.saveEmployee(employee))
                //then
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Employee already exists with email: " + employee.getEmail());

        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void givenEmployeeList_whenFindAll_thenReturnEmployeeList() {
        //given
        Employee employee2 = Employee.builder()
                .id(2L)
                .firstName("Kim")
                .lastName("Sejun")
                .email("test2@gmail.com")
                .build();
        given(employeeRepository.findAll()).willReturn(List.of(employee, employee2));

        //when

        //then
        assertThat(employeeService.getAllEmployees()).isNotNull();
        assertThat(employeeService.getAllEmployees().size()).isEqualTo(2);
    }

    @Test
    void givenEmployeeList_whenFindAll_thenReturnEmptyList() {
        //given
        given(employeeRepository.findAll()).willReturn(List.of());
        //when
        List<Employee> allEmployees = employeeService.getAllEmployees();

        //then
        assertThat(allEmployees).isNotNull();
        assertThat(allEmployees.size()).isEqualTo(0);
    }

    @Test
    void givenEmployeeObject_whenUpdateEmployee_thenReturnEmployee() {
        //given
        given(employeeRepository.save(employee)).willReturn(employee);
        //when
        employee.setFirstName("Updated Sejun");
        employee.setLastName("Updated Kim");
        employee.setEmail("Updated@gmail.com");
        employeeService.updateEmployee(employee);

        //then
        assertThat(employee.getFirstName()).isEqualTo("Updated Sejun");
        assertThat(employee.getLastName()).isEqualTo("Updated Kim");
        assertThat(employee.getEmail()).isEqualTo("Updated@gmail.com");
    }

    @Test
    void givenEmployeeId_whenDeleteEmployee_thenExecuteDeleteByIdMethod() {
        //given
        long id = 1L;
        willDoNothing().given(employeeRepository).deleteById(id);

        //when
        employeeService.deleteEmployee(id);

        //then
        verify(employeeRepository, times(1)).deleteById(id);
    }
}