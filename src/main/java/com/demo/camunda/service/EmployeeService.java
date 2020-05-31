package com.demo.camunda.service;

import com.demo.camunda.entity.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> findByUsername(String username);

    Employee save(Employee employee);
}
