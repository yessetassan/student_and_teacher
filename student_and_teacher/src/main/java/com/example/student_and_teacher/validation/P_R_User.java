package com.example.student_and_teacher.validation;


import com.example.student_and_teacher.dto.PersonDTO;
import com.example.student_and_teacher.services.StudentService;
import com.example.student_and_teacher.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;


@Controller
public class P_R_User {

    private TeacherService teacherService;
    private StudentService studentService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public P_R_User(TeacherService teacherService, StudentService studentService, PasswordEncoder passwordEncoder) {
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isDuplicateUsername(String username) {
        System.out.println(teacherService == null);
        return studentService.findAll().stream().anyMatch(
                x -> x.getUsername().equals(username)
        ) || teacherService.findAll().stream().anyMatch(
                x -> x.getUsername().equals(username)
        );
    }

    public boolean isDuplicateEmail(String email) {
        System.out.println(teacherService == null);
        return studentService.findAll().stream().anyMatch(
                x -> x.getEmail().equals(email)
        ) || teacherService.findAll().stream().anyMatch(
                x -> x.getEmail().equals(email)
        );
    }


    public boolean isStudent(Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch(
                x -> x.getAuthority().equals("ROLE_STUDENT")
        );
    }

    public void validation_settings(PersonDTO changed, PersonDTO initial, BindingResult result) {

        System.out.println(initial.getEmail() + " : " + changed.getEmail());
        if (!changed.getUsername().equals(initial.getUsername()) && isDuplicateUsername(changed.getUsername())) {
            result.rejectValue("username", "", "UserName Must be Unique !");
        }

        if (changed.getBirth_year().isAfter(LocalDate.now())) {
            result.rejectValue("birth_year", "", "Birth year can not be in future !");
        }

        if (!changed.getEmail().equals(initial.getEmail()) && isDuplicateEmail(changed.getEmail())) {
            result.rejectValue("email", "", "Email Should be unique !");
        }
    }

    public boolean isPasswordMatch(String raw, String enc) {
        return passwordEncoder.matches(raw, enc);
    }

    public boolean isValidPassword(String password) {

//        String regex = "^(?=.*[0-9])"
//                + "(?=.*[a-z])(?=.*[A-Z])"
//                + "(?=.*[@#$%^&+=])"
//                + "(?=\\S+$).{8,20}$";
//
//        Pattern p = Pattern.compile(regex);
//
//        Matcher matcher = p.matcher(password);
//
//        return matcher.matches();

        return true;

    }
}