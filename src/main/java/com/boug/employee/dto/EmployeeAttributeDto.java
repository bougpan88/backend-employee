package com.boug.employee.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeAttributeDto {

    @NotNull
    @Size(min = 1)
    private String attributeName;
    @NotNull
    @Size(min = 1)
    private String attributeValue;

    public EmployeeAttributeDto(@NotNull @Size(min = 1) String attributeName, @NotNull @Size(min = 1) String attributeValue) {
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeAttributeDto that = (EmployeeAttributeDto) o;
        return Objects.equals(attributeName, that.attributeName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(attributeName);
    }

    @Override
    public String toString() {
        return "EmployeeAttributeDto{" +
                "attributeName='" + attributeName + '\'' +
                ", attributeValue='" + attributeValue + '\'' +
                '}';
    }
}
