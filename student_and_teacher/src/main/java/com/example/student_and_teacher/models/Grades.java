package com.example.student_and_teacher.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "grades")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Grades implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            nullable = false,
            unique = true,
            updatable = false
    )
    private Integer id;

    @Column (
            name = "course_id",
            nullable = false
    )
    private Integer courseId;

    @Column (
            name = "section_id",
            nullable = false
    )
    private Integer sectionId;

    @Column (
            name = "event_name",
            nullable = false,
            unique = true
    )
    private String eventName;

    @Column (
            name = "score"
    )
    private Double score;


    @Override
    public int hashCode() {
        return Objects.hash(id, courseId, sectionId,  eventName, score);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Grades grades = (Grades) obj;
        return Objects.equals(id, grades.id) &&
                Objects.equals(courseId, grades.courseId) &&
                Objects.equals(sectionId, grades.sectionId) &&
                Objects.equals(eventName, grades.eventName) &&
                Objects.equals(score, grades.score);
    }

    @Override
    public String toString() {
        return "Grades{" +
                "id=" + id +
                ", courseId=" + courseId +
                ", sectionId=" + sectionId +
                ", eventName='" + eventName + '\'' +
                ", score=" + score +
                '}';
    }
}
