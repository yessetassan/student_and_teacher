package com.example.student_and_teacher.models;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "task")
@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class Task {

    @Id
    @Column(
            name = "id",
            nullable = false,
            unique = true,
            updatable = false
    )
    private Integer Id;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(
            name = "published_time",
            nullable = false
    )
    private LocalDateTime published_time;

    @ManyToOne()
    @JoinTable(
            name = "task_teacher",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    @JsonManagedReference
    private Teacher teacher;

    @ManyToOne()
    @JoinTable(
            name = "task_course",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    @JsonManagedReference
    private Course course;

    @ManyToOne()
    @JoinTable(
            name = "task_section",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "section_id")
    )
    @JsonManagedReference
    private Section section;




}
