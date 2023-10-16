package com.example.student_and_teacher.repo;


import com.example.student_and_teacher.models.Time;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeRepo  extends JpaRepository<Time, Integer> {

    Time findTimeByName(String name);
}
