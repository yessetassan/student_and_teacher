package com.example.student_and_teacher.controllers;


import com.example.student_and_teacher.models.Role;
import com.example.student_and_teacher.services.StudentService;
import com.example.student_and_teacher.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@PreAuthorize("hasAnyRole('ROLE_TEACHER', 'ROLE_STUDENT')")
public class PageController {

    private final TeacherService teacherService;
    private final StudentService studentService;

    @Autowired
    public PageController(TeacherService teacherService, StudentService studentService) {
        this.teacherService = teacherService;
        this.studentService = studentService;
    }

    @GetMapping("")
    public String main(Authentication authentication, Principal principal) {

        return authentication.getAuthorities() + " : " + principal.getName();
    }

}
