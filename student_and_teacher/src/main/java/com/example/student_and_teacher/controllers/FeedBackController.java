package com.example.student_and_teacher.controllers;


import com.example.student_and_teacher.models.Student;
import com.example.student_and_teacher.models.Teacher;
import com.example.student_and_teacher.services.FeedBackService;
import com.example.student_and_teacher.services.StudentService;
import com.example.student_and_teacher.services.TeacherService;
import com.example.student_and_teacher.validation.P_R_User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@PreAuthorize("hasAnyRole('ROLE_TEACHER', 'ROLE_STUDENT')")
public class FeedBackController {

    private final FeedBackService feedBackService;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final P_R_User p_r_user;
    private Student student;
    private Teacher teacher;
    private String username;

    @Autowired
    public FeedBackController(FeedBackService feedBackService, StudentService studentService, TeacherService teacherService, P_R_User pRUser) {
        this.feedBackService = feedBackService;
        this.studentService = studentService;
        this.teacherService = teacherService;
        p_r_user = pRUser;
    }

    @GetMapping("/feedback")
    public String feedback(Principal principal,
                           Authentication authentication,
                           Model model) {
        username = principal.getName();
        student = studentService.findByUsername(username);
        teacher = teacherService.findByUsername(username);

        if (p_r_user.isStudent(authentication)) {
            model.addAttribute("student", student);
            return "student/feedback";
        }
        model.addAttribute("teacher", teacher);

        return "teacher/feedback";
    }

}
