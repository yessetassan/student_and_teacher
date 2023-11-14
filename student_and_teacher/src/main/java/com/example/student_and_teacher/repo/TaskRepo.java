package com.example.student_and_teacher.repo;

import com.example.student_and_teacher.models.Section;
import com.example.student_and_teacher.models.Student;
import com.example.student_and_teacher.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;



@Repository
public interface TaskRepo extends JpaRepository<Task, Integer> {

    @Query("select t from Task t where t.section_id = ?1")
    List<Task> findBySectionId(Integer sectionId);

    @Query("select t from Task t where t.Id = ?1")
    Task findById1(Integer event);
}