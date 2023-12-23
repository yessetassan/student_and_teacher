package com.example.student_and_teacher.validation;


import com.example.student_and_teacher.dto.PersonDTO;
import com.example.student_and_teacher.models.Section;
import com.example.student_and_teacher.models.Time;
import com.example.student_and_teacher.services.SectionService;
import com.example.student_and_teacher.services.StudentService;
import com.example.student_and_teacher.services.TeacherService;
import com.example.student_and_teacher.services.TimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;


@Controller @Slf4j
public class P_R_User {

    private TeacherService teacherService;
    private StudentService studentService;
    private final PasswordEncoder passwordEncoder;
    private final TimeService timeService;
    private final SectionService sectionService;

    @Autowired
    public P_R_User(TeacherService teacherService, StudentService studentService, PasswordEncoder passwordEncoder, TimeService timeService, SectionService sectionService) {
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.passwordEncoder = passwordEncoder;
        this.timeService = timeService;
        this.sectionService = sectionService;
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

    public void validation_profile(PersonDTO changed, PersonDTO initial, BindingResult result) {

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

    public boolean checkTimes(Set<String> times) {

        for (String s : times)
            if (timeService.findByName(s) == null) {
                log.info("{} is not found", s);
                return false;
            }

        return true;
    }

    public boolean checkIntersectTimes(Section section, Set<String> times) {

        Section lecture;
        if (section.getType().equals("P")) {
            lecture = sectionService.findByName(section.getRelated_section_name());
        } else {
            lecture = section;
        }

        Set<Section> sections = sectionService.findAll().stream().filter(
                x -> x.getRelated_section_name() != null &&
                        x.getRelated_section_name().equals(lecture.getName())
        ).collect(Collectors.toSet());
        sections.add(lecture);

        for (Section sec : sections) {
            for (Time s : sec.getTimes()) {
                if (times.contains(s.getName())) {
                    log.info("{} is intersected !" , sec);
                    return false;
                }
            }
        }

        return true;
    }

    public String month_form(int month) {
        return switch (month) {
            case 1 -> "January";
            case 2 -> "February";
            case 3 -> "March";
            case 4 -> "April";
            case 5 -> "May";
            case 6 -> "June";
            case 7 -> "July";
            case 8 -> "August";
            case 9 -> "September";
            case 10 -> "October";
            case 11 -> "November";
            default -> "December";
        };
    }
    public String date_form2(String s) {
        int month = Integer.parseInt(s.substring(5,7)),
                day = Integer.parseInt(s.substring(8,10)),
                year = Integer.parseInt(s.substring(0,4)),
                hour = Integer.parseInt(s.substring(11,13)),
                minute = Integer.parseInt(s.substring(14,16));
        boolean pm = hour > 12;
        if (pm) hour -= 12;
        return day + " " + month_form(month) + " " + year + ", " +
                (hour >= 10 ? hour : "0" + hour) + ":"+ (minute > 10 ? minute : "0" + minute) + " " + (pm? "PM" : "AM");
    }


}