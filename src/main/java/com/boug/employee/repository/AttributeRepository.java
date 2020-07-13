package com.boug.employee.repository;

import com.boug.employee.domain.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttributeRepository extends JpaRepository<Attribute, Long> {

    Optional<Attribute> findByName(String attributeName);
}
