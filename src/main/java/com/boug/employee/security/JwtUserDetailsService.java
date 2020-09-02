package com.boug.employee.security;

import com.boug.employee.domain.ApplicationUser;
import com.boug.employee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public JwtUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        ApplicationUser applicationUser = userRepository.getUserByUsername(username);

        if (applicationUser == null){
            throw new UsernameNotFoundException("ApplicationUser not found with username: " + username);
        }
        return new JwtUserDetails(applicationUser);
    }
}
