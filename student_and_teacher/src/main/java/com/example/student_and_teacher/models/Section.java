package com.example.student_and_teacher.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "section")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
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
            name = "current_quota",
            nullable = false
    )
    private Integer current_quota;
    @Column(
            name = "type",
            nullable = false
    )
    private String type;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(
            name = "time",
            nullable = false
    )
    private LocalDateTime time;
    @Column(
            name = "availability",
            nullable = false
    )
    private Boolean availability = false;


    @ManyToOne()
    @JsonManagedReference
    @JoinTable(
            name = "section_course",
            joinColumns = @JoinColumn(name = "section_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Course course;

    @ManyToOne
    @JsonManagedReference
    @JoinTable(
            name = "section_teacher",
            joinColumns = @JoinColumn(name = "section_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private Teacher teacher;


    @ManyToMany(mappedBy = "student_sections")
    @JsonBackReference
    private Set<Student> students = new HashSet<>();


    @OneToMany(mappedBy = "section")
    @JsonBackReference
    private Set<Task> tasks = new HashSet<>();

}

