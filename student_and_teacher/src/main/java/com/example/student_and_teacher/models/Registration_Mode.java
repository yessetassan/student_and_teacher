package com.example.student_and_teacher.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Table(name = "registration_mode")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Registration_Mode implements Serializable {

    @Id
    @Column(
            name = "username",
            nullable = false,
            unique = true
    )
    private String username;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(
            name = "opens",
            nullable = false
    )
    private LocalDateTime opens;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(
            name = "closes",
            nullable = false
    )
    private LocalDateTime closes;

    @Column(
            name = "entered",
            nullable = false
    )
    private Boolean entered = false;



}
