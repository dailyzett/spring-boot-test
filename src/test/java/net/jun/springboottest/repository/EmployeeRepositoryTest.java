package net.jun.springboottest.repository;

import net.jun.springboottest.model.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
}