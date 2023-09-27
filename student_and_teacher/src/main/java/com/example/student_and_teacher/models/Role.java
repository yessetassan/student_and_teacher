package com.example.student_and_teacher.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "role")
@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            nullable = false,
            unique = true,
            updatable = false
    )
    private Integer id;
    @Column(
            name = "name",
            nullable = false
    )
    private String name;

    @ManyToMany(mappedBy = "roles_student")
    @JsonBackReference
    Set<Student> students = new HashSet<>();

    @ManyToMany( mappedBy = "roles_teacher")
    @JsonBackReference
    Set<Teacher> teachers = new HashSet<>();

    public Role(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        if (name != null)
            return this.getName().hashCode();
        return 0;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
