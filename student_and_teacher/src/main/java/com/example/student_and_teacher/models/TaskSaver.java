package com.example.student_and_teacher.models;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "task_saver")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TaskSaver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(
            name = "task_id",
            nullable = false
    )
    private Integer task_id;

    @Column(
            name = "student_id",
            nullable = false
    )
    private Integer student_id;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "submitted")
    private LocalDateTime submitted;

}
