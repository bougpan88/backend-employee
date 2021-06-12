package com.boug.employee.endpoints;

import com.boug.employee.converter.EmployeeConverter;
import com.boug.employee.domain.Employee;
import com.boug.employee.dto.EmployeeAttributeDto;
import com.boug.employee.dto.EmployeeDto;
import com.boug.employee.dto.EmployeeWithIdDto;
import com.boug.employee.error.ApplicationException;
import com.boug.employee.error.CustomError;
import com.boug.employee.service.EmployeeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@CrossOrigin(value = {"http://localhost:4200", "http://localhost", "http://localhost:4200" }, methods = {GET,POST,PUT,DELETE})
@RestController()
@RequestMapping("employees")
public class EmployeeController {

    private EmployeeService employeeService;
    private EmployeeConverter employeeConverter;

    public EmployeeController(EmployeeService employeeService, EmployeeConverter employeeConverter){
        this.employeeService = employeeService;
        this.employeeConverter = employeeConverter;
    }

    @ApiOperation(value = "Used to create a new employee")
    @ApiResponses(value = { @ApiResponse(code = 200 , message = ""),
            @ApiResponse(code = 400 , message = "bad request (Bad input data with analytical description)"),
            @ApiResponse(code = 401 , message = "unauthorized"),
            @ApiResponse(code = 500 , message = "server error")})
    @PostMapping()
    public ResponseEntity<EmployeeWithIdDto> createEmployee(@Valid @RequestBody EmployeeDto employeeDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> message = new ArrayList<>();
            for (FieldError e : errors) {
                message.add("@" + e.getField() + ":" + e.getDefaultMessage());
            }
            throw new ApplicationException( new CustomError(400, "Bad Request", message.toString()));
        } else {
            //Check if duplicate attributes have been posted
            Set<String> duplicates = findDuplicates(employeeDto.getEmployeeAttributes(),EmployeeAttributeDto::getAttributeName);
            if (duplicates.isEmpty()) {
                Employee employee = employeeService.createEmployee(employeeDto);
                return ResponseEntity.ok(employeeConverter.employeeToEmployeeDto(employee));
            } else {
                throw new ApplicationException( new CustomError(400, "Bad Request",
                        "Duplicate Attributes are not allowed: "+duplicates));
            }
        }
    }

    @ApiOperation(value = "Used to update an existing employee")
    @ApiResponses(value = { @ApiResponse(code = 200 , message = ""),
            @ApiResponse(code = 400 , message = "bad request (Bad input data with analytical description)"),
            @ApiResponse(code = 401 , message = "unauthorized"),
            @ApiResponse(code = 500 , message = "server error")})
    @PutMapping(path = "/{employeeId}")
    public ResponseEntity<EmployeeWithIdDto> updateEmployee(@Valid @RequestBody EmployeeDto employeeDto,
                                                            @PathVariable Long employeeId, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> message = new ArrayList<>();
            for (FieldError e : errors) {
                message.add("@" + e.getField() + ":" + e.getDefaultMessage());
            }
            throw new ApplicationException( new CustomError(400, "Bad Request", message.toString()));
        } else {
            //Check if duplicate attributes have been posted
            Set<String> duplicates = findDuplicates(employeeDto.getEmployeeAttributes(),EmployeeAttributeDto::getAttributeName);
            if (duplicates.isEmpty()) {
                Employee employee = employeeService.updateEmployee(employeeDto, employeeId);
                return ResponseEntity.ok(employeeConverter.employeeToEmployeeDto(employee));
            } else {
                throw new ApplicationException( new CustomError(400, "Bad Request",
                        "Duplicate Attributes are not allowed: "+duplicates));
               }
        }
    }

    @ApiOperation(value = "Used to retrieve an employee")
    @ApiResponses(value = { @ApiResponse(code = 200 , message = ""),
            @ApiResponse(code = 400 , message = "bad request (Employee does not exist)"),
            @ApiResponse(code = 401 , message = "unauthorized"),
            @ApiResponse(code = 500 , message = "server error")})
    @GetMapping(path = "/{employeeId}")
    public ResponseEntity<EmployeeWithIdDto> getEmployee(@PathVariable Long employeeId) {
        Employee retrievedEmployee = employeeService.getEmployee(employeeId);
        return ResponseEntity.ok(employeeConverter.employeeToEmployeeDto(retrievedEmployee));
    }

    @ApiOperation(value = "Used to retrieve all employees that are bound with a specific attribute with a specific attribute value")
    @ApiResponses(value = { @ApiResponse(code = 200 , message = ""),
            @ApiResponse(code = 400 , message = "bad request (request parameter missing)"),
            @ApiResponse(code = 401 , message = "unauthorized"),
            @ApiResponse(code = 500 , message = "server error")})
    @GetMapping(path = "/search")
    public ResponseEntity<List<EmployeeWithIdDto>> getEmployeeByAttributeSearch(@RequestParam String attributeName,
                                                                                @RequestParam String attributeValue) {
        List<Employee> employees = employeeService.getEmployeesFromAttribute(attributeName, attributeValue);

        List<EmployeeWithIdDto> employeeWithIdDtos = new ArrayList<>();
        employees.forEach(employee -> employeeWithIdDtos.add(employeeConverter.employeeToEmployeeDto(employee)));
        return ResponseEntity.ok(employeeWithIdDtos);
    }


    @ApiOperation(value = "Used to retrieve all employees")
    @ApiResponses(value = { @ApiResponse(code = 200 , message = ""),
            @ApiResponse(code = 204 , message = "No content"),
            @ApiResponse(code = 401 , message = "unauthorized"),
            @ApiResponse(code = 500 , message = "server error")})
    @GetMapping
    public ResponseEntity<List<EmployeeWithIdDto>> getAllEmployees(){
        List<Employee> employees = employeeService.getAllEmployees();
        if (employees.isEmpty()){
            return ResponseEntity.noContent().build();
        } else{
            List<EmployeeWithIdDto> employeeWithIdDtos = new ArrayList<>();
            employees.forEach(employee -> employeeWithIdDtos.add(employeeConverter.employeeToEmployeeDto(employee)));
            return ResponseEntity.ok(employeeWithIdDtos);
        }
    }

    @ApiOperation(value = "Used as a light api call to retrieve all employee ids that exist")
    @ApiResponses(value = { @ApiResponse(code = 200 , message = ""),
            @ApiResponse(code = 401 , message = "unauthorized"),
            @ApiResponse(code = 500 , message = "server error")})
    @GetMapping(path = "/ids")
    public ResponseEntity<List<Long>> getAllEmployeeIds(){
        List<Employee> employees = employeeService.getAllEmployees();
        if (employees.isEmpty()){
            return ResponseEntity.noContent().build();
        } else{
            List<Long> ids = employees.stream().map(Employee::getId).sorted().collect(Collectors.toList());
            return ResponseEntity.ok(ids);
        }
    }

    @ApiOperation(value = "Used to delete an employee")
    @ApiResponses(value = { @ApiResponse(code = 204 , message = "deleted"),
            @ApiResponse(code = 400 , message = "bad request (Employee does not exist or bad input data)"),
            @ApiResponse(code = 401 , message = "unauthorized"),
            @ApiResponse(code = 500 , message = "server error")})
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long employeeId) {
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
