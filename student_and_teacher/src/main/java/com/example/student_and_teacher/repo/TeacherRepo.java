package com.example.student_and_teacher.repo;

import com.example.student_and_teacher.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepo extends JpaRepository<Teacher, Integer> {
    Teacher findByUsername(String username);

    @Query("select t from Teacher t where t.id = ?1")
    Teacher findId(Integer id);
}
