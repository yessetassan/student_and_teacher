package com.example.student_and_teacher.repo;

import com.example.student_and_teacher.models.Quiz_Saver;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface Quiz_SaverRepo extends CrudRepository<Quiz_Saver, Integer> {

    @Query("select count(distinct q.attempt_order) from Quiz_Saver q where q.student_id = ?1 and q.quiz_id = ?2")
    Integer attempt_count(Integer studentId, Integer quizId);

    @Query("select q from Quiz_Saver q where q.student_id = ?1 and q.quiz_id = ?2")
    List<Quiz_Saver> all(Integer studentId, Integer quizId);
}

