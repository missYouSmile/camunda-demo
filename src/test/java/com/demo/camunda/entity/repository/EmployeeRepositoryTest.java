package com.demo.camunda.entity.repository;

import com.demo.camunda.BaseTest;
import com.demo.camunda.entity.Employee;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EmployeeRepositoryTest extends BaseTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    public void findAll() {
        Iterable<Employee> list = employeeRepository.findAll();
        Assertions.assertThat(list).isEmpty();
    }

}