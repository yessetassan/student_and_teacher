package com.example.student_and_teacher.controllers;

import com.example.student_and_teacher.models.*;
import com.example.student_and_teacher.services.*;
import com.example.student_and_teacher.validation.P_R_User;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@PreAuthorize("hasAnyRole('ROLE_TEACHER', 'ROLE_STUDENT')")
@Slf4j
@RequestMapping("/grades")
public class GradesController {
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final Registration_ModeService registrationModeService;
    private final QuizService quizService;

    private final P_R_User pRUser;
    private static Student student;
    private static Teacher teacher;
    private String username = "";
    private Boolean entered;
    private Boolean isStudent;
    private final SectionService sectionService;


    @Autowired
    public GradesController(TeacherService teacherService, StudentService studentService, Registration_ModeService registrationModeService, ModelMapper modelMapper, P_R_User pRUser, CourseService courseService, QuizService quizService, SectionService sectionService) {
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.registrationModeService = registrationModeService;
        this.pRUser = pRUser;
        this.quizService = quizService;
        this.sectionService = sectionService;
    }

    @GetMapping("")
    public String grades(Authentication authentication,
                           Principal principal,
                           Model model) {
        username = principal.getName();
        teacher = teacherService.findByUsername(username);
        student = studentService.findByUsername(username);
        entered = registrationModeService.findByUsername(username) != null ?
                registrationModeService.findByUsername(username).getEntered() : false;
        isStudent = isStudent(authentication);

        if (isStudent) {
            entered = registrationModeService.findByUsername(username).getEntered();
            student_grades(student, model);
            return "student/grades";
        }

        return "teacher/grades";
    }

    private void student_grades(Student student, Model model) {
        List<Section> sections = sectionService.findAll().stream().filter(
                x -> x.getStudents().contains(student)).toList();
        model.addAttribute("sections",sections);
        model.addAttribute("entered",entered);
        model.addAttribute("student", student);
    }


    private boolean isStudent(Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch(
                x -> x.getAuthority().equals("ROLE_STUDENT")
        );
    }


}

