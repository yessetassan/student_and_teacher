package com.example.student_and_teacher.controllers;


import com.example.student_and_teacher.models.Course;
import com.example.student_and_teacher.models.Role;
import com.example.student_and_teacher.models.Student;
import com.example.student_and_teacher.services.StudentService;
import com.example.student_and_teacher.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Controller
@PreAuthorize("hasAnyRole('ROLE_TEACHER', 'ROLE_STUDENT')")
public class PageController {

    private final TeacherService teacherService;
    private final StudentService studentService;

    @Autowired
    public PageController(TeacherService teacherService, StudentService studentService) {
        this.teacherService = teacherService;
        this.studentService = studentService;
    }

    @GetMapping("/dashboard")
    public String main(Authentication authentication,
                       Principal principal,
                       Model model) {

        if (isStudent(authentication)) {

            student_dashboard(principal, model, studentService);
            return "student/dashboard";
        }

        teacher_dashboard(principal, model, teacherService);
        return "teacher/dashboard";
    }

    static void student_dashboard(Principal principal, Model model, StudentService studentService) {
        Student student = studentService.findByUsername(principal.getName());
        model.addAttribute("student" , student);

        Set<Course> courses = new HashSet<>();
        student.getStudent_sections().forEach(x -> courses.add(x.getCourse()));
        model.addAttribute("courses",courses);
    }

    static void teacher_dashboard(Principal principal, Model model, TeacherService teacherService) {
        // TODO
    }

    private boolean isStudent(Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch(
                x -> x.getAuthority().equals("ROLE_STUDENT")
        );
    }

}
