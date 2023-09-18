package com.example.student_and_teacher.services;


import com.example.student_and_teacher.models.Teacher;
import com.example.student_and_teacher.repo.TeacherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @Transactional
public class TeacherService {

    private final TeacherRepo teacherRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public TeacherService(TeacherRepo teacherRepo, PasswordEncoder passwordEncoder) {
        this.teacherRepo = teacherRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Teacher> findAll() {
        return teacherRepo.findAll();
    }

    public void save(Teacher teacher) {
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        teacherRepo.save(teacher);
    }
}
