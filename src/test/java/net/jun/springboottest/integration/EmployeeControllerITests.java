package net.jun.springboottest.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jun.springboottest.model.Employee;
import net.jun.springboottest.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerITests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
    }

    @Test
    void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        //given
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("JohnDoe@gmail.com")
                .build();
        //when
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        //then
        response.andDo(print())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.email", is("JohnDoe@gmail.com")));
    }

    @Test
    void givenListOfEmployee_whenGetAllEmployee_thenReturnEmployeeList() throws Exception {
        //given
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("JohnDoe@gmail.com")
                .build());

        employeeList.add(Employee.builder()
                .firstName("Kong")
                .lastName("Dosh")
                .email("KongDosh@gmail.com")
                .build());
        employeeRepository.saveAll(employeeList);

        //when
        ResultActions response = mockMvc.perform(get("/api/employees")
                .contentType(APPLICATION_JSON));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)));
    }

    @Test
    void givenEmployeeObject_whenGetEmployeeByID_thenReturnEmployeeObject() throws Exception {
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("JohnDoe@gmail.com")
                .build();

        employeeRepository.save(employee);

        mockMvc.perform(get("/api/employees/{id}", employee.getId())
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @Test
    void givenEmployeeObject_whenGetEmployeeByID_thenNotFoundEmployeeObject() throws Exception {
        //when
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", 1L)
                .contentType(APPLICATION_JSON));

        //then
        response.andDo(print())
                .andExpect(status().isNotFound());
    }
}
