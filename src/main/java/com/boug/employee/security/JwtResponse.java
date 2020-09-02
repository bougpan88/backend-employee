package com.boug.employee.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class JwtResponse implements Serializable
{

    private String jwtToken;
    private String username;
    private Date accountExpire;
    private Date registerDate;
    private boolean enabled;

    public JwtResponse(String jwtToken, String username, Date accountExpire, boolean enabled, Date registerDate) {
        this.jwtToken = jwtToken;
        this.username = username;
        this.accountExpire = accountExpire;
        this.enabled  = enabled;
        this.registerDate = registerDate;
    }
}
