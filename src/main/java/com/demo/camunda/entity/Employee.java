package com.demo.camunda.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String salt;

    @Column(name = "create_at")
    private Date createAt;

    @Column(name = "update_at")
    private Date updateAt;
}
