package com.boug.employee.converter;

import com.boug.employee.domain.Attribute;
import com.boug.employee.domain.Employee;
import com.boug.employee.domain.EmployeeAttribute;
import com.boug.employee.dto.EmployeeAttributeDto;
import com.boug.employee.dto.EmployeeWithIdDto;
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

    public EmployeeWithIdDto employeeToEmployeeDto (Employee employee){
        EmployeeWithIdDto employeeWithIdDto = new EmployeeWithIdDto();
        employeeWithIdDto.setId(employee.getId());
        employeeWithIdDto.setName(employee.getName());
        employeeWithIdDto.setAddress(employee.getAddress());
        employeeWithIdDto.setBirthDate(employee.getBirthDate());
        employeeWithIdDto.setHasCar(employee.getHasCar());
        employeeWithIdDto.setHireDate(employee.getHireDate());
        employeeWithIdDto.setSupervisorId(employee.getSupervisor()!= null ? employee.getSupervisor().getId() : null);

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
            employeeWithIdDto.setEmployeeAttributes(employeeAttributeDtos);
            return employeeWithIdDto;
        } else {
            CustomError customError = new CustomError(400, "Bad Request", errorMessage.toString());
            throw new ApplicationException(customError);
        }
    }


}
