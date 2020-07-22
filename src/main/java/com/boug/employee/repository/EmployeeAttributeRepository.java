package com.boug.employee.repository;

import com.boug.employee.domain.EmployeeAttribute;
import com.boug.employee.domain.EmployeeAttributeId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeAttributeRepository extends JpaRepository<EmployeeAttribute, EmployeeAttributeId> {

    List<EmployeeAttribute> findByEmployeeAttributeIdAttributeIdAndAttributeValue(Long attributeId, String attributeValue);

    void deleteByEmployeeAttributeIdEmployeeId(Long employeeId);
}
