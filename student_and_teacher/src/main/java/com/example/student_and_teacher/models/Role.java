package com.example.student_and_teacher.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "role"
)
@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            nullable = false,
            unique = true
    )
    private Integer id;
    @Column(
            name = "name",
            nullable = false
    )
    private String name;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles_student")
    @JsonBackReference
    Set<Student> students = new HashSet<>();
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles_teacher")
    @JsonBackReference
    Set<Teacher> teachers = new HashSet<>();
}
