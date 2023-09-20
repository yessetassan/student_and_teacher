package com.example.student_and_teacher.controllers;


import com.example.student_and_teacher.models.Student;
import com.example.student_and_teacher.models.Teacher;
import com.example.student_and_teacher.services.StudentService;
import com.example.student_and_teacher.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.time.LocalDate;

@Controller
@RequestMapping
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WelcomeController {
    private final StudentService studentService;
    private final TeacherService teacherService;

    @Autowired
    public WelcomeController(StudentService studentService, TeacherService teacherService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
    }


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register_student")
    public String register_student(@Valid @ModelAttribute("student")  Student student) {
        return "register_student";
    }

    @PostMapping("/register_student")
    public String register_student_post(@Valid @ModelAttribute("student")  Student student,
                                        BindingResult result) {

        if (isDuplicateUsername(student.getUsername())) {
            result.rejectValue("username", "", "Username is already exists !");
        }

        if (student.getBirth_year().isAfter(LocalDate.now())) {
            result.rejectValue("birth_year", "", "Birth year can not be in future !");
        }

        if (result.hasErrors()) {
            return "register_student";
        }

        studentService.save(student);
        return "/login";
    }



    @GetMapping("/register_teacher")
    public String register_teacher(@Valid @ModelAttribute("teacher") Teacher teacher) {
        return "register_teacher";
    }

    @PostMapping("/register_teacher")
    public String register_teacher_post(@Valid @ModelAttribute("teacher") Teacher teacher,
                                   BindingResult result) {

        if (isDuplicateUsername(teacher.getUsername())) {
            result.rejectValue("username", "", "Username is already exists !");
        }

        if (teacher.getBirth_year().isAfter(LocalDate.now())) {
            result.rejectValue("birth_year", "", "Birth year can not be in future !");
        }

        if (result.hasErrors()) {
            return "register_teacher";
        }

        teacherService.save(teacher);
        return "/login";
    }

    public boolean isDuplicateUsername(String username) {
        return studentService.findAll().stream().anyMatch(
                x -> x.getUsername().equals(username)
        ) || teacherService.findAll().stream().anyMatch(
                x -> x.getUsername().equals(username)
        );
    }

}
