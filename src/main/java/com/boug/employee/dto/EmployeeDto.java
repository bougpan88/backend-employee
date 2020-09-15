package com.boug.employee.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeDto {

    @NotNull
    @Size(min = 1, max = 100)
    private String name;
    @NotNull
    private LocalDate hireDate;
    @NotNull
    @Size(min = 1)
    private String address;
    @NotNull
    private Boolean hasCar;
    @NotNull
    private LocalDate birthDate;
    private List<EmployeeAttributeDto> employeeAttributes = new ArrayList<>();
    private Long supervisorId;


    public EmployeeDto(@NotNull @Size(min = 1, max = 100) String name, @NotNull LocalDate hireDate,
                             @NotNull @Size(min = 1) String address, @NotNull Boolean hasCar, @NotNull LocalDate birthDate,
                             List<EmployeeAttributeDto> employeeAttributes, Long supervisorId) {
        this.name = name;
        this.hireDate = hireDate;
        this.address = address;
        this.hasCar = hasCar;
        this.birthDate = birthDate;
        this.employeeAttributes = employeeAttributes;
        this.supervisorId = supervisorId;
    }

}
