package com.boug.employee.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class EmployeeAttributeId implements Serializable {

    @Column(name = "EMP_ID")
    @NotNull
    private Long employeeId;
    @Column(name = "ATTR_ID")
    @NotNull
    private Long attributeId;


    public EmployeeAttributeId(@NotNull Long employeeId, @NotNull Long attributeId) {
        this.employeeId = employeeId;
        this.attributeId = attributeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeAttributeId that = (EmployeeAttributeId) o;
        return Objects.equals(employeeId, that.employeeId) &&
                Objects.equals(attributeId, that.attributeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, attributeId);
    }

    @Override
    public String toString() {
        return "EmployeeAttributeId{" +
                "employeeId=" + employeeId +
                ", attributeId='" + attributeId + '\'' +
                '}';
    }
}
