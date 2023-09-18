package com.example.student_and_teacher.services;


import com.example.student_and_teacher.models.Teacher;
import com.example.student_and_teacher.repo.TeacherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @Transactional
public class TeacherService {

    private final TeacherRepo teacherRepo;

    @Autowired
    public TeacherService(TeacherRepo teacherRepo) {
        this.teacherRepo = teacherRepo;

    }

    public List<Teacher> findAll() {
        return teacherRepo.findAll();
    }
}
