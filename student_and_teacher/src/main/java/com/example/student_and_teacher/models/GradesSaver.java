package com.example.student_and_teacher.models;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "grades_saver")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GradesSaver {

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
            name = "grades_id",
            nullable = false
    )
    private Integer grades_id;

    @Column (
            name = "student_id",
            nullable = false
    )
    private Integer studentId;

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
    public String toString() {
        return "GradesSaver{" +
                "id=" + id +
                ", grades_id=" + grades_id +
                ", studentId=" + studentId +
                ", userScore=" + userScore +
                ", percentage=" + percentage +
                ", userPercentage=" + userPercentage +
                '}';
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (studentId != null ? studentId.hashCode() : 0);
        result = 31 * result + (userScore != null ? userScore.hashCode() : 0);
        result = 31 * result + (percentage != null ? percentage.hashCode() : 0);
        result = 31 * result + (userPercentage != null ? userPercentage.hashCode() : 0);
        return result;
    }
}
