package com.example.student_and_teacher.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne
    @JoinColumn(name = "section_id" , referencedColumnName = "id")
    @JsonIgnore
    private Section section;




}
