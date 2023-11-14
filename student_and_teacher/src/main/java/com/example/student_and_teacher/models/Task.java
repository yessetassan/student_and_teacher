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
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "task")
@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            updatable = false
    )
    private Integer Id;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(
            name = "published_time",
            nullable = false
    )
    private LocalDateTime published_time;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(
            name = "opens"
    )
    private LocalDateTime opens;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(
            name = "closes"
    )
    private LocalDateTime closes;

    @Column(
            name = "link_path",
            columnDefinition = "TEXT"
    )
    private String link_path;
    @Column(
            name = "file_path",
            columnDefinition = "TEXT"
    )
    private String file_path;

    @Column(
            name = "message",
            columnDefinition = "TEXT"
    )
    private String message;

    @Column(
            name = "pre_message",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String pre_message;

    @Column(
            name = "section_id",
            columnDefinition = "TEXT"
    )
    private Integer section_id;

    @Override
    public String toString() {
        return "Task{" +
                "Id=" + Id +
                ", published_time=" + published_time +
                ", link_path='" + link_path + '\'' +
                ", file_path='" + file_path + '\'' +
                ", message='" + message + '\'' +
                '}';
    }


}
