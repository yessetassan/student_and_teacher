package com.example.student_and_teacher.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name = "section") @Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class Section implements Serializable{

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
            name = "location",
            nullable = false
    )
    private String location;
    @Column(
            name = "name",
            unique = true
    )
    private String name;

    @Column(
            name = "total_quota",
            nullable = false
    )
    private Integer total_quota;
    @Column(
            name = "current_quota"
    )
    private Integer current_quota;
    @Column(
            name = "type",
            nullable = false
    )
    private String type;
    @Column(
            name = "related_section_name"
    )
    private String related_section_name;

    @Column(
            name = "availability",
            nullable = false,
            columnDefinition = "false"
    )
    private Boolean availability = false;

    @ManyToOne()
    @JoinColumn(name = "course_id" , referencedColumnName = "id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "teacher_id" , referencedColumnName = "id")
    private Teacher teacher;

    @ManyToMany
    @JoinTable(
            name = "section_times",
            joinColumns = @JoinColumn(name = "section_id"),
            inverseJoinColumns = @JoinColumn(name = "time_id")
    )
    private Set<Time> times;


    @ManyToMany(mappedBy = "student_sections")
    private Set<Student> students = new HashSet<>();

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", location='" + location + '\'' +
                ", name='" + name + '\'' +
                ", total_quota=" + total_quota +
                ", current_quota=" + current_quota +
                ", type='" + type + '\'' +
                ", availability=" + availability +
                ", course=" + course +
                '}';
    }

}

