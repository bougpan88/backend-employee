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
public class EmployeeWithIdDto extends EmployeeDto{

    private Long id;

    public EmployeeWithIdDto(@NotNull @Size(min = 1, max = 100) String name, @NotNull LocalDate hireDate, @NotNull @Size(min = 1) String address, @NotNull Boolean hasCar, @NotNull LocalDate birthDate, List<EmployeeAttributeDto> employeeAttributes, Long supervisorId, Long id) {
        super(name, hireDate, address, hasCar, birthDate, employeeAttributes, supervisorId);
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeWithIdDto that = (EmployeeWithIdDto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "EmployeeWithIdDto{" +
                "id='" + id + '\'' +
                "name='" + getName() + '\'' +
                ", hireDate=" + getHireDate() +
                ", address='" + getAddress() + '\'' +
                ", hasCar=" + getHasCar() +
                ", birthDate=" + getBirthDate() +
                ", employeeAttributes=" + getEmployeeAttributes() +
                ", supervisorId=" + getSupervisorId() +
                '}';
    }


}
