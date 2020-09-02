package com.boug.employee.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "EmployeeAttribute")
@Getter
@Setter
@NoArgsConstructor
public class EmployeeAttribute {

    @EmbeddedId
    @NotNull
    private EmployeeAttributeId employeeAttributeId;

    @NotNull
    @Size(min = 1)
    @Column(name = "ATTR_Value")
    private String attributeValue;

    public EmployeeAttribute(@NotNull EmployeeAttributeId employeeAttributeId, @NotNull @Size(min = 1) String attributeValue) {
        this.employeeAttributeId = employeeAttributeId;
        this.attributeValue = attributeValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeAttribute that = (EmployeeAttribute) o;
        return Objects.equals(employeeAttributeId, that.employeeAttributeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeAttributeId);
    }

    @Override
    public String toString() {
        return "EmployeeAttribute{" +
                "employeeAttributeId=" + employeeAttributeId +
                ", attributeValue='" + attributeValue + '\'' +
                '}';
    }
}
