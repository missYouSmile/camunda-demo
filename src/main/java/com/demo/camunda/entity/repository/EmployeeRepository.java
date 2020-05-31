package com.demo.camunda.entity.repository;

import com.demo.camunda.entity.Employee;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {

    List<Employee> findByUsername(String username);
}
