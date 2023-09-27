package com.example.student_and_teacher.services;


import com.example.student_and_teacher.models.Role;
import com.example.student_and_teacher.models.Student;
import com.example.student_and_teacher.repo.StudentRepo;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service @Transactional @Slf4j
public class StudentService {

    private final StudentRepo studentRepo;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private Role role;
    @Autowired
      public StudentService(StudentRepo studentRepo, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.studentRepo = studentRepo;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    public List<Student> findAll() {
        return studentRepo.findAll();
    }

    public void save(Student student) {
        roleService.findAll().stream().filter(
                x -> x.getName().equals("ROLE_STUDENT")
        ).forEach(x -> role = x);

        student.getRoles_student().add(role);
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        studentRepo.save(student);
    }

    public Student findByUsername(String username) {
        return studentRepo.findByUsername(username);
    }

    public void simple_save(Student student) {
        log.info("Student is updated -> {}" , student);
        studentRepo.save(student);
    }
}
