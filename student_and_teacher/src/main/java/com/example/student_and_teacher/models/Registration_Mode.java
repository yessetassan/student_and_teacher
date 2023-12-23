package com.example.student_and_teacher.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(name = "registration_mode")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Registration_Mode implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            nullable = false,
            unique = true
    )
    private Integer id;

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

    @Override
    public String toString() {
        return "Registration_Mode{" +
                "username='" + username + '\'' +
                ", opens=" + opens +
                ", closes=" + closes +
                ", entered=" + entered +
                '}';
    }
    @Override
    public int hashCode() {
        return Objects.hash(username, opens, closes, entered);
    }


}
