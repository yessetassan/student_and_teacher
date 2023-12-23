package com.example.student_and_teacher.controllers;


import com.example.student_and_teacher.models.Privacy;
import com.example.student_and_teacher.models.Registration_Mode;
import com.example.student_and_teacher.models.Student;
import com.example.student_and_teacher.models.Teacher;
import com.example.student_and_teacher.services.PrivacyService;
import com.example.student_and_teacher.services.Registration_ModeService;
import com.example.student_and_teacher.services.StudentService;
import com.example.student_and_teacher.services.TeacherService;
import com.example.student_and_teacher.validation.P_R_User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequestMapping
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class WelcomeController {
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final P_R_User pRUser;
    private final PrivacyService privacyService;
    private final Registration_ModeService registrationModeService;

    public WelcomeController(StudentService studentService, TeacherService teacherService, P_R_User pRUser, PrivacyService privacyService, Registration_ModeService registrationModeService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.pRUser = pRUser;
        this.privacyService = privacyService;
        this.registrationModeService = registrationModeService;
    }


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register_student")
    public String register_student(@Valid @ModelAttribute("student")  Student student) {
        return "register_student";
    }

    @GetMapping("/register_teacher")
    public String register_teacher(@Valid @ModelAttribute("teacher") Teacher teacher) {
        return "register_teacher";
    }

    @PostMapping("/register_student")
    public String register_student_post(@Valid @ModelAttribute("student")  Student student,
                                        BindingResult result) {



        boolean dup_username = pRUser.isDuplicateUsername(student.getUsername()),
                after_birth = student.getBirth_year().isAfter(LocalDate.now()),
                dup_email = pRUser.isDuplicateEmail(student.getEmail());

        System.out.println("Student{" +
                "duplicateUsername=" + dup_username +
                ", afterCurrentYearBirth=" + after_birth +
                ", duplicateEmail=" + dup_email +
                '}');

        if (dup_username)
            result.rejectValue("username", "", "Username is already exists !");
        if (after_birth)
            result.rejectValue("birth_year", "", "Birth year can not be in future !");
        if (dup_email)
            result.rejectValue("email", "", "Email Should be unique !");

        if (result.hasErrors()) {
            return "register_student";
        }

        studentService.save(student);

        registrationModeService.save(new Registration_Mode(null,
                student.getUsername(),
                LocalDateTime.now(),
                LocalDateTime.now()
                ,false));
        privacyService.save(new Privacy(null,
                student.getUsername(),
                true
        ,true));

        log.info("{} is student signed up !", student);

        return "login";
    }

    @PostMapping("/register_teacher")
    public String register_teacher_post(@Valid @ModelAttribute("teacher")  Teacher teacher,
                                        BindingResult result) {



        boolean dup_username = pRUser.isDuplicateUsername(teacher.getUsername()),
                after_birth = teacher.getBirth_year().isAfter(LocalDate.now()),
                dup_email = pRUser.isDuplicateEmail(teacher.getEmail());

        System.out.println("teacher{" +
                "duplicateUsername=" + dup_username +
                ", afterCurrentYearBirth=" + after_birth +
                ", duplicateEmail=" + dup_email +
                '}');

        if (dup_username)
            result.rejectValue("username", "", "Username is already exists !");
        if (after_birth)
            result.rejectValue("birth_year", "", "Birth year can not be in future !");
        if (dup_email)
            result.rejectValue("email", "", "Email Should be unique !");

        if (result.hasErrors()) {
            return "register_teacher";
        }

        teacherService.save(teacher);

        registrationModeService.save(new Registration_Mode(null,
                teacher.getUsername(),
                LocalDateTime.now(),
                LocalDateTime.now()
                ,false));
        privacyService.save(new Privacy(null,
                teacher.getUsername(),
                true
                ,true));

        log.info("{} is teacher signed up !", teacher);

        return "login";
    }
    

}
