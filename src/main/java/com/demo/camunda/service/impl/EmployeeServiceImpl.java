package com.demo.camunda.service.impl;

import com.demo.camunda.entity.Employee;
import com.demo.camunda.entity.repository.EmployeeRepository;
import com.demo.camunda.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<Employee> findByUsername(String username) {
        return employeeRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }
}
