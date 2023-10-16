package com.example.student_and_teacher.repo;

import com.example.student_and_teacher.models.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SectionRepo extends JpaRepository<Section, Integer> {

    Optional<Section> findById(Integer id);

    Section findByName(String name);

    @Query("select s from Section s where s.id = ?1")
    Section findId(Integer id);
}
