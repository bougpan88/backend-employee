package com.boug.employee.converter;

import com.boug.employee.domain.Attribute;
import com.boug.employee.domain.Employee;
import com.boug.employee.domain.EmployeeAttribute;
import com.boug.employee.dto.EmployeeAttributeDto;
import com.boug.employee.dto.EmployeeDto;
import com.boug.employee.error.ApplicationException;
import com.boug.employee.error.CustomError;
import com.boug.employee.repository.AttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class EmployeeConverter {

    private AttributeRepository attributeRepository;

    @Autowired
    public EmployeeConverter(AttributeRepository attributeRepository){
        this.attributeRepository = attributeRepository;
    }

    public EmployeeDto employeeToEmployeeDto (Employee employee){
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(employee.getId());
        employeeDto.setName(employee.getName());
        employeeDto.setAddress(employee.getAddress());
        employeeDto.setBirthDate(employee.getBirthDate());
        employeeDto.setHasCar(employee.getHasCar());
        employeeDto.setHireDate(employee.getHireDate());
        employeeDto.setSupervisorId(employee.getSupervisor()!= null ? employee.getSupervisor().getId() : null);

        List<EmployeeAttributeDto> employeeAttributeDtos = new ArrayList<>();
        Set<EmployeeAttribute> employeeAttributes = employee.getEmployeeAttributes();
        StringBuilder errorMessage = new StringBuilder();

        for (EmployeeAttribute employeeAttribute : employeeAttributes){
            EmployeeAttributeDto employeeAttributeDto = new EmployeeAttributeDto();
            employeeAttributeDto.setAttributeValue(employeeAttribute.getAttributeValue());
            //Query Database to find name of Attribute from it's id
            Long attributeId = employeeAttribute.getEmployeeAttributeId().getAttributeId();
            Optional<Attribute> attribute = attributeRepository.findById(attributeId);
            if (attribute.isPresent()){
                employeeAttributeDtos.add(new EmployeeAttributeDto(attribute.get().getName(), employeeAttribute.getAttributeValue()));
            } else {
                errorMessage.append(employeeAttributeDto.getAttributeName()).append("Attribute with Id:")
                                                                            .append(attributeId)
                                                                            .append(" does not exist.");
            }
        }
        if (errorMessage.toString().isEmpty()){
            employeeDto.setEmployeeAttributes(employeeAttributeDtos);
            return employeeDto;
        } else {
            CustomError customError = new CustomError(400, "Bad Request", errorMessage.toString());
            throw new ApplicationException(customError);
        }
    }


}
