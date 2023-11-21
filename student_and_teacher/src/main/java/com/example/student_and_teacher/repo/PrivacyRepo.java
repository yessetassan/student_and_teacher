package com.example.student_and_teacher.repo;

import com.example.student_and_teacher.models.Privacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PrivacyRepo extends JpaRepository<Privacy, Integer> {

    Privacy findByUsername(String username);
}
