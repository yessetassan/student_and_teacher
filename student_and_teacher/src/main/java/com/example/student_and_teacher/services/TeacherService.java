package com.example.student_and_teacher.services;


import com.example.student_and_teacher.models.Role;
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
    private final RoleService roleService;
    private Role role;

    @Autowired
    public TeacherService(TeacherRepo teacherRepo, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.teacherRepo = teacherRepo;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    public List<Teacher> findAll() {
        return teacherRepo.findAll();
    }

    public void save(Teacher teacher) {
        roleService.findAll().stream().filter(
                x -> x.getName().equals("ROLE_TEACHER")
        ).forEach(x -> role = x);

        teacher.getRoles_teacher().add(role);
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        teacherRepo.save(teacher);
    }
}
