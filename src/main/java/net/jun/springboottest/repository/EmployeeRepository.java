package net.jun.springboottest.repository;

import net.jun.springboottest.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    @Query("SELECT e FROM Employee e WHERE e.firstName = ?1 and e.lastName = ?2")
    Employee findByJPQL(String firstName, String lastName);

    @Query("SELECT e FROM Employee e WHERE e.firstName = :firstName and e.lastName = :lastName")
    Employee findByJPQLParam(String firstName, String lastName);

    @Query(value = "SELECT * FROM employees WHERE first_name = ?1 and last_name = ?2", nativeQuery = true)
    Employee findByNativeSQL(String firstName, String lastName);

    @Query(value = "SELECT * FROM employees WHERE first_name = :firstName and last_name = :lastName", nativeQuery = true)
    Employee findByNativeSQLWithParam(String firstName, String lastName);
}
