package com.example.student_and_teacher.repo;

import com.example.student_and_teacher.models.Section;
import com.example.student_and_teacher.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SectionRepo extends JpaRepository<Section, Integer> {

    Optional<Section> findById(Integer id);

    Section findByName(String name);

    @Query("select s from Section s where s.id = ?1")
    Section findId(Integer id);
    @Query("select s from Section  s")
    Set<Section> all();
}
