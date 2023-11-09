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
@Table(name = "quiz_saver")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Quiz_Saver implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            nullable = false,
            unique = true
    )
    private Integer id;

    @Column(
            name = "quiz_id",
            nullable = false
    )
    private Integer quiz_id;

    @Column(
            name = "quiz_list_id",
            nullable = false
    )
    private Integer quiz_list_id;

    @Column(
            name = "user_answer",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String user_answer;

    @Column(
            name = "user_score",
            nullable = false
    )
    private Double user_score;

    @Column(
            name = "attempt_order",
            nullable = false
    )
    private Integer attempt_order;

    @Column(
            name = "student_id",
            nullable = false
    )
    private Integer student_id;

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + id;
        result = 31 * result + quiz_id;
        result = 31 * result + quiz_list_id;
        result = 31 * result + (user_answer != null ? user_answer.hashCode() : 0);
        long userScoreBits = Double.doubleToLongBits(user_score);
        result = 31 * result + (int) (userScoreBits ^ (userScoreBits >>> 32));
        result = 31 * result + attempt_order;
        result = 31 * result + student_id;
        return result;
    }

    @Override
    public String toString() {
        return "Quiz_Saver{" +
                "id=" + id +
                ", quiz_id=" + quiz_id +
                ", quiz_list_id=" + quiz_list_id +
                ", user_answer='" + user_answer + '\'' +
                ", user_score=" + user_score +
                ", attempt_order=" + attempt_order +
                ", student_id=" + student_id +
                '}';
    }



}


