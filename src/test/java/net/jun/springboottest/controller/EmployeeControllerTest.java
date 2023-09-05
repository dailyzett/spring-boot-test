package net.jun.springboottest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jun.springboottest.model.Employee;
import net.jun.springboottest.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("JohnDoe@gmail.com")
                .build();
    }

    @Test
    void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        //given
        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArguments()[0]);

        //when
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        //then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @Test
    void givenListOfEmployee_whenGetAllEmployee_thenReturnEmployeeList() throws Exception {
        //given
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("JohnDoe@gmail.com")
                .build();

        Employee employee2 = Employee.builder()
                .firstName("Kong")
                .lastName("Dosh")
                .email("KongDosh@gmail.com")
                .build();
        given(employeeService.getAllEmployees()).willReturn(List.of(employee, employee2));

        //when
        ResultActions response = mockMvc.perform(get("/api/employees")
                .contentType(APPLICATION_JSON));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$[0].email", is(employee.getEmail())))
                .andExpect(jsonPath("$[1].firstName", is(employee2.getFirstName())))
                .andExpect(jsonPath("$[1].lastName", is(employee2.getLastName())))
                .andExpect(jsonPath("$[1].email", is(employee2.getEmail())));
    }

    @Test
    void givenEmployeeObject_whenGetEmployeeByID_thenReturnEmployee() throws Exception {
        //given
        given(employeeService.getEmployeeById(any(Long.class))).willReturn(Optional.of(employee));

        //when
        mockMvc.perform(get("/api/employees/{id}", 1L)
                        .contentType(APPLICATION_JSON))

                //then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @Test
    void givenEmployeeObject_whenGetEmployeeByID_thenNotFoundEmployeeObject() throws Exception {
        //given
        given(employeeService.getEmployeeById(anyLong())).willReturn(Optional.empty());

        //when
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", 1L)
                .contentType(APPLICATION_JSON));

        //then
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenEmployeeObject_whenUpdateEmployeeObject_thenSuccessUpdate() throws Exception {
        //given
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("John@Gmail.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstName("UpdatedJohn")
                .lastName("UpdatedDoe")
                .email("John@Gmail.com")
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
        given(employeeService.updateEmployee(any(Employee.class))).willAnswer((invocation) -> invocation.getArguments()[0]);

        //when
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }

    @Test
    void givenEmployeeObject_whenNotFoundEmployeeObject_thenFailedUpdate() throws Exception {
        //given
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("John@Gmail.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstName("UpdatedJohn")
                .lastName("UpdatedDoe")
                .email("John@Gmail.com")
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        given(employeeService.updateEmployee(any(Employee.class))).willAnswer((invocation) -> invocation.getArguments()[0]);

        //when
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void givenEmployeeObject_whenDeleteEmployeeObject_thenReturn200Status() throws Exception {
        //given
        Employee savedEmployee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("John@Gmail.com")
                .build();
        given(employeeService.getEmployeeById(anyLong())).willReturn(Optional.of(savedEmployee));
        willDoNothing().given(employeeService).deleteEmployee(anyLong());

        //when
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", 1L)
                .contentType(APPLICATION_JSON));

        //then
        response.andDo(print())
                .andExpect(status().isOk());
        verify(employeeService).deleteEmployee(anyLong());
    }
}