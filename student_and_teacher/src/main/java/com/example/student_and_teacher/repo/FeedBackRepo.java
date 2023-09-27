package com.example.student_and_teacher.repo;


import com.example.student_and_teacher.models.FeedBack;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedBackRepo extends CrudRepository<FeedBack, Integer> {

}
