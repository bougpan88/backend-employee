package com.boug.employee.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeDto {

    private Long id;
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
    private List<EmployeeAttributeDto> employeeAttributes;
    private Long supervisorId;


    public EmployeeDto(Long id, @NotNull @Size(min = 1, max = 100) String name, @NotNull LocalDate hireDate,
                       @NotNull @Size(min = 1) String address, @NotNull Boolean hasCar, @NotNull LocalDate birthDate,
                       List<EmployeeAttributeDto> employeeAttributes, Long supervisorId) {
        this.id = id;
        this.name = name;
        this.hireDate = hireDate;
        this.address = address;
        this.hasCar = hasCar;
        this.birthDate = birthDate;
        this.employeeAttributes = employeeAttributes;
        this.supervisorId = supervisorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeDto that = (EmployeeDto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "EmployeeDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", hireDate=" + hireDate +
                ", address='" + address + '\'' +
                ", hasCar=" + hasCar +
                ", birthDate=" + birthDate +
                ", employeeAttributes=" + employeeAttributes +
                ", supervisorId=" + supervisorId +
                '}';
    }
}
