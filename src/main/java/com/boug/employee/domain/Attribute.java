package com.boug.employee.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "attribute")
@Getter
@Setter
@NoArgsConstructor
public class Attribute {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ATTR_ID")
    private Long id;

    /**
     * We want to be able to change the name for each attribute. Therefore name can not be primary key.
     * Name must however be unique.
     */
    @Column(name = "ATTR_Name", unique = true)
    @NotNull
    @Size(min = 1, max = 50)
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "employeeAttributeId.attributeId", cascade = CascadeType.REMOVE)
    private Set<EmployeeAttribute> employeeAttributes;

    public Attribute(Long id, @NotNull @Size(min = 1, max = 50) String name, Set<EmployeeAttribute> employeeAttributes) {
        this.id = id;
        this.name = name;
        this.employeeAttributes = employeeAttributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attribute attribute = (Attribute) o;
        return Objects.equals(id, attribute.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", employeeAttributes=" + employeeAttributes +
                '}';
    }
}

