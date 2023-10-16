package com.example.student_and_teacher.repo;

import com.example.student_and_teacher.models.Student;
import com.example.student_and_teacher.services.StudentService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface StudentRepo extends JpaRepository<Student, Integer> {
    Student findByUsername(String username);
    @Query("select s from Student s join fetch s.student_sections where s.username like ?1")
    Student findSections(String username);


}
