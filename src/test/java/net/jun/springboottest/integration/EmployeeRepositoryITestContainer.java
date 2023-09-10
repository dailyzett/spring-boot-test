package net.jun.springboottest.integration;

import net.jun.springboottest.model.Employee;
import net.jun.springboottest.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeRepositoryITestContainer extends AbstractContainerBaseTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
        employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("John@gmail.com")
                .build();
    }

    //Junit test for save employee operation BDD Style
    @Test
    @DisplayName("save employee 작업을 Junit으로 테스트")
    void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
        //given

        //when
        Employee savedEmployee = employeeRepository.save(employee);

        //then
        assertThat(savedEmployee.getId()).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    @Test
    void givenEmployeeList_whenFindAll_thenEmployeesList() {
        //given
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

        //when
        employeeRepository.save(employee);

        //then
        employeeRepository.findById(employee.getId())
                .ifPresentOrElse(employee -> {
                    assertThat(employee).isNotNull();
                    assertThat(employee.getId()).isEqualTo(employee.getId());
                }, () -> {
                    throw new RuntimeException("Employee not found");
                });
    }

    @Test
    void givenEmail_whenFindByEmail_thenReturnEmployeeObject() {
        //given
        Employee employee3 = employeeRepository.save(employee);

        //when
        employeeRepository.findByEmail(employee3.getEmail()).ifPresentOrElse(employee -> {
            //then
            assertThat(employee).isNotNull();
            assertThat(employee.getEmail()).isEqualTo(employee3.getEmail());
        }, () -> {
            throw new RuntimeException("Employee not found");
        });
    }

    @Test
    void givenEmployeeObject_whenUpdate_thenSuccessUpdateForObject() {
        //given
        employeeRepository.save(employee);

        //when
        employeeRepository.findById(employee.getId()).ifPresentOrElse(employee1 -> {
            employee1.setFirstName("Change");
            employee1.setLastName("Test");
            employee1.setEmail("Change@gmail.com");
        }, () -> {
            throw new RuntimeException("Employee not found");
        });
        //then
        employeeRepository.findById(employee.getId()).ifPresentOrElse(employee1 -> {
            assertThat(employee1).isNotNull();
            assertThat(employee1.getFirstName()).isEqualTo("Change");
            assertThat(employee1.getLastName()).isEqualTo("Test");
            assertThat(employee1.getEmail()).isEqualTo("Change@gmail.com");
        }, () -> {
            throw new RuntimeException("Employee not found");
        });
    }

    @Test
    void givenEmployeeObject_whenDelete_thenSuccessDeleteForObject() {
        //given
        employeeRepository.save(employee);

        //when
        employeeRepository.delete(employee);

        //then
        employeeRepository.findById(employee.getId()).ifPresentOrElse(employee1 -> {
            throw new RuntimeException("Employee not found");
        }, () -> {
            assertThat(true).isTrue();
        });
    }

    @Test
    void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject() {
        //given
        employeeRepository.save(employee);

        //when
        Employee byJPQL = employeeRepository.findByJPQL(employee.getFirstName(), employee.getLastName());

        //then
        assertThat(byJPQL).isNotNull();
        assertThat(byJPQL.getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(byJPQL.getLastName()).isEqualTo(employee.getLastName());
        assertThat(byJPQL.getEmail()).isEqualTo(employee.getEmail());
    }

    @Test
    void givenFirstNameAndLastName_whenFindByJPQLParams_thenReturnEmployeeObject() {
        //given
        employeeRepository.save(employee);
        //when
        Employee byJPQLParam = employeeRepository.findByJPQLParam(employee.getFirstName(), employee.getLastName());

        //then
        assertThat(byJPQLParam).isNotNull();
        assertThat(byJPQLParam.getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(byJPQLParam.getLastName()).isEqualTo(employee.getLastName());
        assertThat(byJPQLParam.getEmail()).isEqualTo(employee.getEmail());
    }

    @Test
    void givenFirstNameAndLastName_whenFindByNativeQuery_thenReturnEmployeeObject() {
        //given
        employeeRepository.save(employee);

        //when
        Employee byNativeSQL = employeeRepository.findByNativeSQL(employee.getFirstName(), employee.getLastName());

        //then
        assertThat(byNativeSQL).isNotNull();
        assertThat(byNativeSQL.getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(byNativeSQL.getLastName()).isEqualTo(employee.getLastName());
        assertThat(byNativeSQL.getEmail()).isEqualTo(employee.getEmail());
    }

    @Test
    void givenFirstNameAndLastName_whenFindByNativeQueryWithParams_thenReturnEmployeeObject() {
        //given
        employeeRepository.save(employee);

        //when
        Employee byNativeSQLWithParam = employeeRepository.findByNativeSQLWithParam(employee.getFirstName(), employee.getLastName());

        //then
        assertThat(byNativeSQLWithParam).isNotNull();
        assertThat(byNativeSQLWithParam.getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(byNativeSQLWithParam.getLastName()).isEqualTo(employee.getLastName());
        assertThat(byNativeSQLWithParam.getEmail()).isEqualTo(employee.getEmail());
    }
}