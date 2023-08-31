package net.jun.springboottest.repository;

import net.jun.springboottest.model.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    //Junit test for save employee operation BDD Style
    @Test
    @DisplayName("save employee 작업을 Junit으로 테스트")
    void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
        //given
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("John@gmail.com")
                .build();

        //when
        Employee savedEmployee = employeeRepository.save(employee);

        //then
        assertThat(savedEmployee.getId()).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    @Test
    void givenEmployeeList_whenFindAll_thenEmployeesList() {
        //given
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("John@gmail.com")
                .build();

        Employee employee1 = Employee.builder()
                .firstName("Ramesh")
                .lastName("Ramesh")
                .email("Ramesh@gmail.com")
                .build();

        Employee employee3 = Employee.builder()
                .firstName("Duck")
                .lastName("Kim")
                .email("Kim@gmail.com")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee1);
        employeeRepository.save(employee3);

        //when
        List<Employee> allEmployees = employeeRepository.findAll();

        //then
        assertThat(allEmployees).isNotNull();
        assertThat(allEmployees.size()).isEqualTo(3);
    }

    @Test
    void givenEmployee_whenFindById_thenReturnEmployeeObject() {
        //given
        Employee employee3 = Employee.builder()
                .firstName("Duck")
                .lastName("Kim")
                .email("Kim@gmail.com")
                .build();

        //when
        employeeRepository.save(employee3);

        //then
        employeeRepository.findById(employee3.getId())
                .ifPresentOrElse(employee -> {
                    assertThat(employee).isNotNull();
                    assertThat(employee.getId()).isEqualTo(employee3.getId());
                }, () -> {
                    throw new RuntimeException("Employee not found");
                });
    }
}