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
            nullable = false,
            unique = true
    )
    private Integer courseId;

    @Column (
            name = "section_id",
            nullable = false,
            unique = true
    )
    private Integer sectionId;

    @Column (
            name = "student_id",
            nullable = false,
            unique = true
    )
    private Integer studentId;

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

    @Column (
            name = "user_score"
    )
    private Double userScore;

    @Column (
            name = "percentage"
    )
    private Double percentage;

    @Column (
            name = "user_percentage"
    )
    private Double userPercentage;

    @Override
    public int hashCode() {
        return Objects.hash(id, courseId, sectionId, studentId, eventName, score, userScore, percentage, userPercentage);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Grades grades = (Grades) obj;
        return Objects.equals(id, grades.id) &&
                Objects.equals(courseId, grades.courseId) &&
                Objects.equals(sectionId, grades.sectionId) &&
                Objects.equals(studentId, grades.studentId) &&
                Objects.equals(eventName, grades.eventName) &&
                Objects.equals(score, grades.score) &&
                Objects.equals(userScore, grades.userScore) &&
                Objects.equals(percentage, grades.percentage) &&
                Objects.equals(userPercentage, grades.userPercentage);
    }

    @Override
    public String toString() {
        return "Grades{" +
                "id=" + id +
                ", courseId=" + courseId +
                ", sectionId=" + sectionId +
                ", studentId=" + studentId +
                ", eventName='" + eventName + '\'' +
                ", score=" + score +
                ", userScore=" + userScore +
                ", percentage=" + percentage +
                ", userPercentage=" + userPercentage +
                '}';
    }
}
