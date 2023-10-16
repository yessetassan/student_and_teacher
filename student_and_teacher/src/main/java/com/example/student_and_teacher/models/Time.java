package com.example.student_and_teacher.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Table(name = "time")
@Entity  @Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class Time {

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
            nullable = false,
            unique = true
    )
    private String name;

    @ManyToMany(mappedBy = "times")
    @JsonIgnore
    private Set<Section> set = new HashSet<>();

    public Time(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Time{" +
                "name=" + name +
                '}';
    }
}
