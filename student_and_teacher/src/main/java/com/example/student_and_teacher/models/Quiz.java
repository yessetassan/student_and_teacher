package com.example.student_and_teacher.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "quiz")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Quiz implements Serializable {

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
            unique = true,
            columnDefinition = "TEXT"
    )
    private String name;

    @Column(
            name = "quiz_list_id",
            nullable = false
    )
    private Integer quiz_list_id;

    @Column(
            name = "description",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String description;

    @Column(
            name = "attempts",
            nullable = false
    )
    private Integer attempts;

    @Column(
            name = "section_id",
            nullable = false
    )
    private Integer section_id;

    @Column(
            name = "time_limit",
            nullable = false
    )
    private Integer time_limit;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(
            name = "opens",
            nullable = false
    )
    private LocalDateTime opens;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(
            name = "closes",
            nullable = false
    )
    private LocalDateTime closes;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(
            name = "created",
            nullable = false
    )
    private LocalDateTime created;



    @Override
    public String toString() {
        return "Quiz{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", attempts=" + attempts +
                ", section_id=" + section_id +
                ", time_limit=" + time_limit +
                ", opens=" + opens +
                ", closes=" + closes +
                ", created=" + created +
                '}';
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (attempts != null ? attempts.hashCode() : 0);
        result = 31 * result + (section_id != null ? section_id.hashCode() : 0);
        result = 31 * result + (time_limit != null ? time_limit.hashCode() : 0);
        result = 31 * result + (opens != null ? opens.hashCode() : 0);
        result = 31 * result + (closes != null ? closes.hashCode() : 0);
        result = 31 * result + (created != null ? created.hashCode() : 0);
        return result;
    }


}