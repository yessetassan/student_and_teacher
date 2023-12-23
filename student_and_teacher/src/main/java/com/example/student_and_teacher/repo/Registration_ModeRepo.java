package com.example.student_and_teacher.repo;


import com.example.student_and_teacher.models.Registration_Mode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Registration_ModeRepo extends JpaRepository<Registration_Mode, String> {

    @Query("select r from Registration_Mode r where r.username = ?1")
    Registration_Mode findByUsername(String username);
}
