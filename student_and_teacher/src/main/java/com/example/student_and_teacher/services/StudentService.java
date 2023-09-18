package com.example.student_and_teacher.services;


import com.example.student_and_teacher.models.Student;
import com.example.student_and_teacher.repo.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @Transactional
public class StudentService {

    private final StudentRepo studentRepo;
    private final PasswordEncoder passwordEncoder;
    @Autowired
      public StudentService(StudentRepo studentRepo, PasswordEncoder passwordEncoder) {
        this.studentRepo = studentRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Student> findAll() {
        return studentRepo.findAll();
    }

    public void save(Student student) {
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        studentRepo.save(student);
    }
}
