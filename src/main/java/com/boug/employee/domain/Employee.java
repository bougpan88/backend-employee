package com.boug.employee.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "employee")
@Getter
@Setter
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "EMP_ID")
    private Long id;

    @Column(name = "EMP_Name")
    @NotNull
    @Size(min = 1, max = 100)
    private String name;

    /**
    Date of hire makes more sense with a Local Date object instead of Date
     */
    @Column(name = "EMP_DateOfHire")
    @NotNull
    private LocalDate hireDate;

    @Column(name = "EMP_Address")
    @NotNull
    @Size(min = 1)
    private String address;

    @Column(name = "EMP_HasCar")
    @NotNull
    private Boolean hasCar;

    /**
     Date of birth makes more sense with a Local Date object instead of Date
     */
    @Column(name = "EMP_BirthDate")
    @NotNull
    private LocalDate birthDate;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "employeeAttributeId.employeeId")
    private Set<EmployeeAttribute> employeeAttributes;

    @ManyToOne(fetch = FetchType.EAGER)
    private Employee supervisor;


    public Employee(Long id, @NotNull @Size(min = 1, max = 100) String name, @NotNull LocalDate hireDate,
                    @NotNull String address, @NotNull Boolean hasCar, @NotNull LocalDate birthDate,
                    Set<EmployeeAttribute> employeeAttributes, Employee supervisor) {
        this.id = id;
        this.name = name;
        this.hireDate = hireDate;
        this.address = address;
        this.hasCar = hasCar;
        this.birthDate = birthDate;
        this.employeeAttributes = employeeAttributes;
        this.supervisor = supervisor;
    }

    public Employee(@NotNull @Size(min = 1, max = 100) String name, @NotNull LocalDate hireDate, @NotNull @Size(min = 1) String address, @NotNull Boolean hasCar, @NotNull LocalDate birthDate, Set<EmployeeAttribute> employeeAttributes, Employee supervisor) {
        this.name = name;
        this.hireDate = hireDate;
        this.address = address;
        this.hasCar = hasCar;
        this.birthDate = birthDate;
        this.employeeAttributes = employeeAttributes;
        this.supervisor = supervisor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", hireDate=" + hireDate +
                ", address='" + address + '\'' +
                ", hasCar=" + hasCar +
                ", birthDate=" + birthDate +
                ", employeeAttributes=" + employeeAttributes +
                ", supervisor=" + supervisor +
                '}';
    }
}
