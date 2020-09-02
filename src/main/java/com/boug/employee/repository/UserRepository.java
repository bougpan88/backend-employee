package com.boug.employee.repository;

import com.boug.employee.domain.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<ApplicationUser, Long> {

    ApplicationUser getUserByUsername(String username);
}
