package com.example.student_and_teacher.controllers;


import com.example.student_and_teacher.dto.PersonDTO;
import com.example.student_and_teacher.models.Privacy;
import com.example.student_and_teacher.models.Student;
import com.example.student_and_teacher.models.Teacher;
import com.example.student_and_teacher.services.PrivacyService;
import com.example.student_and_teacher.services.StudentService;
import com.example.student_and_teacher.services.TeacherService;
import com.example.student_and_teacher.validation.P_R_User;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@PreAuthorize("hasAnyRole('ROLE_TEACHER', 'ROLE_STUDENT')")
@Slf4j
@RequestMapping("/settings")
public class SettingsController {
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final ModelMapper modelMapper;
    private final PrivacyService privacyService;
    private final P_R_User pRUser;
    private static Student student;
    private static Teacher teacher;
    private String username = "";
    private Boolean isStudent;


    @Autowired
    public SettingsController(TeacherService teacherService, StudentService studentService, ModelMapper modelMapper, PrivacyService privacyService, P_R_User pRUser) {
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.modelMapper = modelMapper;
        this.privacyService = privacyService;
        this.pRUser = pRUser;
    }

    @GetMapping("")
    public String settings(Authentication authentication,
                           Principal principal,
                           Model model) {

        username = principal.getName();
        teacher = teacherService.findByUsername(username);
        student = studentService.findByUsername(username);
        isStudent = pRUser.isStudent(authentication);
        log.info("Username -> {}", username);
        if (pRUser.isStudent(authentication)) {
            student_settings(model, studentService);
            return "student/settings";
        }

        teacher_settings(model, teacherService);
        return "teacher/settings";
    }
    @PostMapping("/info")
    public String settings_info_student(Authentication authentication,
                                        @Valid @ModelAttribute("person_dto") PersonDTO personDTO,
                                        BindingResult result,
                                        @ModelAttribute("student") Student th_student,
                                        @ModelAttribute("photo_edit") Photo_Edit photoEdit,
                                        @ModelAttribute("password_edit") Password_Edit password_edit,
                                        @ModelAttribute("privacy") Privacy privacy,
                                        @ModelAttribute("arr1") String arr1,
                                        @ModelAttribute("arr2") String arr2,
                                        Model model) {

        if (pRUser.isStudent(authentication)) {

            pRUser.validation_settings(personDTO, studentDTOConverter(student), result);
            if (result.hasErrors()) {
                model.addAttribute("arr1", 1);
                return "student/settings";
            }
            Student new_one = complete_student(personDTO);
            studentService.simple_save(new_one);
            Authentication new_auth = new UsernamePasswordAuthenticationToken(
                    new_one.getUsername(), authentication.getCredentials(), authentication.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(new_auth);

            return "redirect:/settings";
        }

        pRUser.validation_settings(personDTO, teacherDTOConverter(teacher), result);

        if (result.hasErrors()) {
            return "teacher/settings";
        }
        Teacher new_one = complete_teacher(personDTO);
        teacherService.simple_save(new_one);

        Authentication new_auth = new UsernamePasswordAuthenticationToken(
                new_one.getUsername(), authentication.getCredentials(), authentication.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(new_auth);

        return "redirect:/settings";
    }

    @PostMapping("/photo")
    public String settings_edit_photo_student(@ModelAttribute("photo_edit") Photo_Edit photoEdit) {
        student.setPhoto(photoEdit.getEdit_photo());
        studentService.simple_save(student);
        return "redirect:/settings";
    }

    @PostMapping("/password")
    public String settings_edit_password_student(Authentication authentication,
                                                 @Valid @ModelAttribute("password_edit") Password_Edit password_edit,
                                                 BindingResult result_password,
                                                 @Valid @ModelAttribute("person_dto") PersonDTO personDTO,
                                                 BindingResult result_person,
                                                 @ModelAttribute("student") Student th_student,
                                                 @ModelAttribute("photo_edit") Photo_Edit photoEdit,
                                                 @ModelAttribute("privacy") Privacy privacy,
                                                 @ModelAttribute("arr1") String arr1,
                                                 @ModelAttribute("arr2") String arr2,
                                                 Model model) {
        String enc = student.getPassword();
        String raw = password_edit.getOld_password();
        boolean old_p_matches = pRUser.isPasswordMatch(raw, enc);
        if (!old_p_matches) {
            result_password.rejectValue("old_password", "","Old Password is Wrong !");
        }
        if (!password_edit.getNew_password().equals(password_edit.getRepeat_password())) {
            result_password.rejectValue("repeat_password", "","With New password does not matches !");
        }

        if (isStudent) {
            if (result_password.hasErrors()) {
                model.addAttribute("arr2", 1);
                return "student/settings";
            }
            student.setPassword(password_edit.getNew_password());
            studentService.save(student);
        }
        else {
            if (result_password.hasErrors()) {
                return "teacher/settings";
            }
        }
        return "redirect:/settings";
    }



    private Teacher complete_teacher(PersonDTO personDTO) {
        Teacher new_one = teacherConverter(personDTO);
        new_one.setRoles_teacher(teacher.getRoles_teacher());
        new_one.setStudents(teacher.getStudents());
        return new_one;
    }

    private Student complete_student(PersonDTO personDTO) {
        Student new_one = studentConverter(personDTO);
        new_one.setRoles_student(student.getRoles_student());
        new_one.setStudent_sections(student.getStudent_sections());
        new_one.setTeachers(student.getTeachers());
        new_one.setPassword(student.getPassword());
        new_one.setPhoto(student.getPhoto());
        new_one.setId(student.getId());
        return new_one;
    }


    void student_settings(Model model,
                          StudentService studentService) {
        model.addAttribute("student" , student);
        model.addAttribute("person_dto" , studentDTOConverter(student));
        model.addAttribute("photo_edit" , new Photo_Edit(student.getPhoto()));
        model.addAttribute("password_edit", new Password_Edit());
        model.addAttribute("sections", studentService.findSectionsByStudentId(student.getId()));
        model.addAttribute("privacy", privacyService.findByUsername(student.getUsername()));
        model.addAttribute("arr1", -1);
        model.addAttribute("arr2", -1);
    }

    void teacher_settings(Model model, TeacherService teacherService) {
        model.addAttribute("teacher" , teacher);
        model.addAttribute("person_dto" , teacherDTOConverter(teacher));
        model.addAttribute("photo_edit" , new Photo_Edit(teacher.getPhoto()));
        model.addAttribute("password_edit", new Password_Edit());
    }

    public PersonDTO studentDTOConverter(Student student) {
        return modelMapper.map(student, PersonDTO.class);
    }
    public PersonDTO teacherDTOConverter(Teacher teacher) {
        return modelMapper.map(teacher, PersonDTO.class);
    }
    public  Student studentConverter(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Student.class);
    }
    public Teacher teacherConverter(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Teacher.class);
    }

}


@AllArgsConstructor @NoArgsConstructor @Setter @Getter @ToString
class Photo_Edit {
    private String edit_photo;
}


@AllArgsConstructor @NoArgsConstructor @Setter @Getter
class Password_Edit {
    private String old_password;
    private String new_password;
    private String repeat_password;
    public String toString() {
        return old_password + " : " + new_password+ " : " + repeat_password;
    }
}



@AllArgsConstructor @NoArgsConstructor @Setter @Getter @ToString
class Privacy_Method {
    private String username;
    private Boolean access_info_others;
    private Boolean access_view_courses;
}


