package com.example.student_and_teacher.repo;

import com.example.student_and_teacher.models.FeedBack;
import com.example.student_and_teacher.models.Quiz_List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface Quiz_ListRepo extends CrudRepository<Quiz_List, Integer> {

    @Query("select count(distinct q.top_id) from Quiz_List q")
    Integer findQuizCount();

    @Query("select q from Quiz_List q")
    List<Quiz_List> all();
}
