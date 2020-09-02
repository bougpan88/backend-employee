package com.boug.employee.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    private String password;

    private boolean enabled;

    private boolean locked;

    private Date accountExpire;

    private Date credentialsExpire;

    private Date registerDate;

}