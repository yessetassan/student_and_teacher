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
@Table(name = "privacy")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Privacy implements Serializable {

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
            name = "username",
            nullable = false,
            unique = true,
            columnDefinition = "TEXT"
    )
    private String username;

    @Column(
            name = "access_myself",
            nullable = false
    )
    private Boolean access_myself = true;

    @Column(
            name = "access_course",
            nullable = false
    )
    private Boolean access_course = true;

    @Override
    public int hashCode() {
        return Objects.hash(id, username, access_myself, access_course);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Privacy privacy = (Privacy) obj;
        return Objects.equals(id, privacy.id) &&
                Objects.equals(username, privacy.username) &&
                Objects.equals(access_myself, privacy.access_myself) &&
                Objects.equals(access_course, privacy.access_course);
    }

    @Override
    public String toString() {
        return "Privacy{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", access_myself=" + access_myself +
                ", access_course=" + access_course +
                '}';
    }

}