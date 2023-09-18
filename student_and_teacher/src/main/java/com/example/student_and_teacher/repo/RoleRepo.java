package com.example.student_and_teacher.repo;

import com.example.student_and_teacher.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepo extends JpaRepository<Role, Integer> {
}
