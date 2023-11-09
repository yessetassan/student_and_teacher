package com.example.student_and_teacher.repo;


import com.example.student_and_teacher.models.Registration_Mode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Registration_ModeRepo extends JpaRepository<Registration_Mode, String> {

    Registration_Mode findByUsername(String username);
}
