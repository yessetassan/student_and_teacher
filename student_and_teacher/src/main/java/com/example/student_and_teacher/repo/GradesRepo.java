package com.example.student_and_teacher.repo;


import com.example.student_and_teacher.models.Grades;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradesRepo extends JpaRepository<Grades, Integer> {

    @Query("select g from Grades g where g.sectionId = ?1")
    List<Grades> findBySectionId(Integer id);
}
