package com.boug.employee.service;

import com.boug.employee.domain.Attribute;
import com.boug.employee.domain.Employee;
import com.boug.employee.domain.EmployeeAttribute;
import com.boug.employee.domain.EmployeeAttributeId;
import com.boug.employee.dto.EmployeeAttributeDto;
import com.boug.employee.dto.EmployeeDto;
import com.boug.employee.error.ApplicationException;
import com.boug.employee.error.CustomError;
import com.boug.employee.repository.AttributeRepository;
import com.boug.employee.repository.EmployeeAttributeRepository;
import com.boug.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;
    private AttributeRepository attributeRepository;
    private EmployeeAttributeRepository employeeAttributeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, EmployeeAttributeRepository employeeAttributeRepository,
                           AttributeRepository attributeRepository){
        this.employeeRepository = employeeRepository;
        this.employeeAttributeRepository = employeeAttributeRepository;
        this.attributeRepository = attributeRepository;
    }

    @Transactional
    public Employee createEmployee(EmployeeDto employeeDto) {

        EmployeeWithValidationError employeeWithValidationError = new EmployeeWithValidationError();
        //Create employeeAttributes from employeeAttributeDtos
        if (employeeDto.getEmployeeAttributes() != null && !employeeDto.getEmployeeAttributes().isEmpty()) {
            employeeWithValidationError = employeeAttributeDtoToEmployAttribute(employeeDto.getEmployeeAttributes(),
                    null,
                    employeeWithValidationError);
        }
        employeeWithValidationError = supervisorIdToSupervisor(employeeWithValidationError, employeeDto.getSupervisorId());

        if (employeeWithValidationError.errorMessage.toString().isEmpty()) {
            Employee employee = new Employee(employeeDto.getName(), employeeDto.getHireDate(),
                    employeeDto.getAddress(), employeeDto.getHasCar(), employeeDto.getBirthDate(),
                    employeeWithValidationError.employeeAttributes,
                    employeeWithValidationError.supervisorEmployee);
            employee = employeeRepository.save(employee);
            for (EmployeeAttribute employeeAttribute : employeeWithValidationError.employeeAttributes) {
                //Until now employeeId is null in each employeeAttribute
                //Now that we have created an employId we must add it to each employeeAttribute before persisting
                employeeAttribute.getEmployeeAttributeId().setEmployeeId(employee.getId());
                employeeAttributeRepository.save(employeeAttribute);
            }
            return employee;
        }
        CustomError customError = new CustomError(400, "Bad Request", employeeWithValidationError.errorMessage.toString());
        throw new ApplicationException(customError);
    }

    @Transactional
    public Employee updateEmployee(EmployeeDto employeeDto) {

        Optional<Employee> optEmployee = employeeRepository.findById(employeeDto.getId());
        EmployeeWithValidationError employeeWithValidationError = new EmployeeWithValidationError();
        if (optEmployee.isPresent()) {
            //Create employeeAttributes from employeeAttributeDtos
            if(employeeDto.getEmployeeAttributes()!= null && !employeeDto.getEmployeeAttributes().isEmpty()) {
                employeeWithValidationError = employeeAttributeDtoToEmployAttribute(employeeDto.getEmployeeAttributes(),
                                                                                    employeeDto.getId(),
                                                                                    employeeWithValidationError);
            }
            employeeWithValidationError = supervisorIdToSupervisor(employeeWithValidationError, employeeDto.getSupervisorId());

            //When everything is ok we can persist employee and employeeAttributes
            if (employeeWithValidationError.errorMessage.toString().isEmpty()){
                Employee employee = new Employee(employeeDto.getId(), employeeDto.getName(), employeeDto.getHireDate(),
                                                 employeeDto.getAddress(), employeeDto.getHasCar(),
                                                 employeeDto.getBirthDate(), employeeWithValidationError.employeeAttributes,
                                                 employeeWithValidationError.supervisorEmployee);
                for (EmployeeAttribute employeeAttribute: employeeWithValidationError.employeeAttributes){
                    employeeAttributeRepository.save(employeeAttribute);
                }
                employee = employeeRepository.save(employee);
                return employee;
            }
        } else {
            employeeWithValidationError.errorMessage.append("Employee does not exist.");
        }
        CustomError customError = new CustomError(400, "Bad Request", employeeWithValidationError.errorMessage.toString());
        throw new ApplicationException(customError);
    }

    public Employee getEmployee(Long employeeId){
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        if (employee.isPresent()){
            return employee.get();
        } else {
            throw new ApplicationException(new CustomError(400, "Bad Request", "Employee does not exist."));
        }
    }

    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    @Transactional
    public void deleteEmployee(Long employeeId){
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        if (employee.isPresent()){
            employeeAttributeRepository.deleteByEmployeeAttributeIdEmployeeId(employeeId);
            employeeRepository.deleteById(employeeId);
        } else {
            throw new ApplicationException(new CustomError(404, "Not Found", "Employee does not exist."));
        }
    }

    private EmployeeWithValidationError employeeAttributeDtoToEmployAttribute(List<EmployeeAttributeDto> employeeAttributeDtoList,
                                                              Long employeeId,
                                                              EmployeeWithValidationError employeeWithValidationError
                                                         ){

        for (EmployeeAttributeDto employeeAttributeDto : employeeAttributeDtoList) {
            Optional<Attribute> optAttribute = attributeRepository.findByName(employeeAttributeDto.getAttributeName());
            if (optAttribute.isPresent()){
                //Only when no error has occurred continue to map employeeAttributeDto to employeeAttribute
                if (employeeWithValidationError.errorMessage.toString().isEmpty()){
                    employeeWithValidationError.employeeAttributes.add(new EmployeeAttribute(new EmployeeAttributeId(employeeId, optAttribute.get().getId())
                            ,employeeAttributeDto.getAttributeValue()));
                }
            } else {
                employeeWithValidationError.errorMessage.append(employeeAttributeDto.getAttributeName()).append(" Attribute does not exist.");
            }
        }
        return employeeWithValidationError;
    }

    private EmployeeWithValidationError supervisorIdToSupervisor(EmployeeWithValidationError employeeWithValidationError, Long supervisorId){
        //Check if supervisor exists
        if (supervisorId != null){
            Optional<Employee> optSupervisor = employeeRepository.findById(supervisorId);
            if (optSupervisor.isPresent()){
                employeeWithValidationError.setSupervisorEmployee(optSupervisor.get());
            } else {
                employeeWithValidationError.errorMessage.append("Supervisor does not exist.");
            }
        }
        return employeeWithValidationError;
    }


    private class EmployeeWithValidationError{
        private StringBuilder errorMessage = new StringBuilder();
        private Set<EmployeeAttribute> employeeAttributes = new HashSet<>();
        private Employee supervisorEmployee = null;

        private void setSupervisorEmployee(Employee supervisorEmployee) {
            this.supervisorEmployee = supervisorEmployee;
        }
    }


}
