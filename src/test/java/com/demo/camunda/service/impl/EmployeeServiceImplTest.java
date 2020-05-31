package com.demo.camunda.service.impl;

import com.demo.camunda.BaseTest;
import com.demo.camunda.entity.Employee;
import com.demo.camunda.service.EmployeeService;
import org.apache.tomcat.util.security.MD5Encoder;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class EmployeeServiceImplTest extends BaseTest {

    @Autowired
    private EmployeeService employeeService;

    @Test
    public void save() throws Exception {
        String password = "123456";
        String salt = UUID.randomUUID().toString();
        String username = "张三";
        Employee entity = new Employee()
                .setUsername(username)
                .setPassword(MD5Encoder.encode(String.format("%s%s", password, salt)
                        .getBytes("utf-8")))
                .setSalt(salt)
                .setCreateAt(new Date());
        employeeService.save(entity);

        List<Employee> employees = employeeService.findByUsername(username);
        Assertions.assertThat(employees).isNotEmpty();
        Assertions.assertThat(employees.size()).isEqualTo(1);
    }
}