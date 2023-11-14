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
@Table(name = "quiz_list")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Quiz_List implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            nullable = false,
            unique = true,
            insertable = false, updatable = false
    )
    private Integer id;

    @Column(
            name = "question",
            nullable = false,
            unique = true,
            columnDefinition = "TEXT"
    )
    private String question;

    @Column(
            name = "options",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String options;

    @Column(
            name = "right_option",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String right_option;

    @Column(
            name = "score",
            nullable = false
    )
    private Double score;

    @Column(
            name = "top_id",
            nullable = false
    )
    private Integer top_id;

    @Column(
            name = "section_id",
            nullable = false
    )
    private Integer section_id;


    @Override
    public String toString() {
        return "Quiz_List{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", options='" + options + '\'' +
                ", right_option='" + right_option + '\'' +
                ", score=" + score +
                '}';
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((question == null) ? 0 : question.hashCode());
        result = prime * result + ((options == null) ? 0 : options.hashCode());
        result = prime * result + ((right_option == null) ? 0 : right_option.hashCode());
        result = prime * result + ((score == null) ? 0 : score.hashCode());
        return result;
    }

}

