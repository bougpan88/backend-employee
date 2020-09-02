package com.boug.employee.security;

import com.boug.employee.domain.ApplicationUser;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Date;

@SuppressWarnings("serial")
public class JwtUserDetails implements org.springframework.security.core.userdetails.UserDetails {

    private ApplicationUser applicationUser;

    public JwtUserDetails(ApplicationUser applicationUser){
        this.applicationUser = applicationUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return applicationUser.getPassword();
    }

    @Override
    public String getUsername() {
        return applicationUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return applicationUser.getAccountExpire().after(new Date());
    }

    @Override
    public boolean isAccountNonLocked() {
        return !applicationUser.isLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return applicationUser.getCredentialsExpire().after(new Date());
    }

    @Override
    public boolean isEnabled() {
        return applicationUser.isEnabled();
    }

    public Date getAccountExpire(){
        return applicationUser.getAccountExpire();
    }

    public Date getRegisterDate(){
        return applicationUser.getRegisterDate();
    }


}
