package com.example.student_and_teacher.repo;

import com.example.student_and_teacher.models.Quiz;
import com.example.student_and_teacher.models.Quiz_List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface QuizRepo extends CrudRepository<Quiz, Integer> {

    @Query("select q from Quiz q")
    List<Quiz> all();
}
