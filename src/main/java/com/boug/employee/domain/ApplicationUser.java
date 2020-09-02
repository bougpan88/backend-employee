package com.boug.employee.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "application_user")
@Getter
@Setter
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    @NotNull
    private String username;

    @NotNull
    private String password;

    private boolean enabled;

    private boolean locked;

    @Column(name = "account_expire")
    private Date accountExpire;

    @Column(name = "credentials_expire")
    private Date credentialsExpire;

    @Column(name = "register_date")
    private Date registerDate;

}