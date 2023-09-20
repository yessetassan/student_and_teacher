package com.example.student_and_teacher.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "feedback")
@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class FeedBack {

    @Id
    @Column(
            name = "username",
            nullable = false
    )
    private String username;

    @Column(
            name = "message",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String message;


}
