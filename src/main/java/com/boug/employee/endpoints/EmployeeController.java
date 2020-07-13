package com.boug.employee.endpoints;

import com.boug.employee.converter.EmployeeConverter;
import com.boug.employee.domain.Attribute;
import com.boug.employee.domain.Employee;
import com.boug.employee.domain.EmployeeAttribute;
import com.boug.employee.dto.AttributeDto;
import com.boug.employee.dto.EmployeeAttributeDto;
import com.boug.employee.dto.EmployeeDto;
import com.boug.employee.error.CustomError;
import com.boug.employee.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toSet;

@RestController()
@RequestMapping("employees")
public class EmployeeController {

    private EmployeeService employeeService;
    private EmployeeConverter employeeConverter;

    public EmployeeController(EmployeeService employeeService, EmployeeConverter employeeConverter){
        this.employeeService = employeeService;
        this.employeeConverter = employeeConverter;
    }

    @PostMapping()
    public ResponseEntity createEmployee(@Valid @RequestBody EmployeeDto employeeDto, BindingResult bindingResult){
        if (bindingResult.hasErrors() || employeeDto.getId() != null) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> message = new ArrayList<>();
            for (FieldError e : errors) {
                message.add("@" + e.getField() + ":" + e.getDefaultMessage());
            }
            message.add("Id will be automatically created. Can not be contained in message body");
            CustomError customError = new CustomError(400, "Bad Request", message.toString());
            return ResponseEntity.badRequest().body(customError);
        } else {
            //Check if duplicate attributes have been posted
            Set<String> duplicates = findDuplicates(employeeDto.getEmployeeAttributes(),EmployeeAttributeDto::getAttributeName);
            if (duplicates.isEmpty()) {
                Employee employee = employeeService.createEmployee(employeeDto);
                return ResponseEntity.ok(employeeConverter.employeeToEmployeeDto(employee));
            } else {
                return ResponseEntity.badRequest().body(new CustomError(400, "Bad Request",
                        "Duplicate Attributes are not allowed: "+duplicates));
            }
        }
    }

    @PutMapping(path = "")
    public ResponseEntity updateEmployee(@Valid @RequestBody EmployeeDto employeeDto, BindingResult bindingResult){
        if (bindingResult.hasErrors() || employeeDto.getId() == null) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> message = new ArrayList<>();
            for (FieldError e : errors) {
                message.add("@" + e.getField() + ":" + e.getDefaultMessage());
            }
            message.add("Id can not be null");
            CustomError customError = new CustomError(400, "Bad Request", message.toString());
            return ResponseEntity.badRequest().body(customError);
        } else {
            //Check if duplicate attributes have been posted
            Set<String> duplicates = findDuplicates(employeeDto.getEmployeeAttributes(),EmployeeAttributeDto::getAttributeName);
            if (duplicates.isEmpty()) {
                Employee employee = employeeService.updateEmployee(employeeDto);
                return ResponseEntity.ok(employeeConverter.employeeToEmployeeDto(employee));
            } else {
                return ResponseEntity.badRequest().body(new CustomError(400, "Bad Request",
                                                                       "Duplicate Attributes are not allowed: "+duplicates));
            }
        }
    }

    @GetMapping(path = "/{employeeId}")
    public ResponseEntity getEmployee(@PathVariable Long employeeId) {
        Employee retrievedEmployee = employeeService.getEmployee(employeeId);
        return ResponseEntity.ok(employeeConverter.employeeToEmployeeDto(retrievedEmployee));
    }

    @GetMapping
    public ResponseEntity getAllEmployees(){
        List<Employee> employees = employeeService.getAllEmployees();
        if (employees.isEmpty()){
            return ResponseEntity.noContent().build();
        } else{
            List<EmployeeDto> employeeDtos = new ArrayList<>();
            employees.forEach(employee -> employeeDtos.add(employeeConverter.employeeToEmployeeDto(employee)));
            return ResponseEntity.ok(employeeDtos);
        }
    }

    @DeleteMapping(path = "/{employeeId}")
    public ResponseEntity deleteEmployee(@PathVariable Long employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }

    private static <T, R> Set<R> findDuplicates(Collection<? extends T> collection, Function<? super T, ? extends R> mapper) {
        Set<R> uniques = new HashSet<>();
        return collection.stream()
                .map(mapper)
                .filter(e -> !uniques.add(e))
                .collect(toSet());
    }

}
