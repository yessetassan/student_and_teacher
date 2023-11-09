package com.example.student_and_teacher.repo;

import com.example.student_and_teacher.models.Section;
import com.example.student_and_teacher.models.Student;
import com.example.student_and_teacher.services.StudentService;
import org.hibernate.annotations.Fetch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


@Repository
public interface StudentRepo extends JpaRepository<Student, Integer> {
    Student findByUsername(String username);
    @Query("select s from Student s join fetch s.student_sections where s.username like ?1")
    Student findSections(String username);


    @Query("select s.student_sections from Student s where s.username = ?1")
    Set<Section> findSectionsByStudentUsername(String username);

}
