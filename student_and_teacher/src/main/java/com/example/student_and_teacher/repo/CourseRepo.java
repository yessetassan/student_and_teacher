package com.example.student_and_teacher.repo;


import com.example.student_and_teacher.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepo extends JpaRepository<Course, Integer> {

    Course findByCode(String code);
}
