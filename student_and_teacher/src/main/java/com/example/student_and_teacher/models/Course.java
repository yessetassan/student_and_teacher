package com.example.student_and_teacher.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "course")
@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class Course implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            nullable = false,
            unique = true,
            updatable = false
    )
    private Integer id;

    @Column (
            name = "name",
            nullable = false,
            unique = true
    )
    private String name;


    @OneToMany(mappedBy = "course")
    private Set<Section> sections;

    @Override
    public int hashCode() {
        return id;
    }
    @Override
    public String toString() {
        return id+" " + name;
    }
}
