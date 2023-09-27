package com.example.student_and_teacher.controllers;


import com.example.student_and_teacher.dto.PersonDTO;
import com.example.student_and_teacher.models.Student;
import com.example.student_and_teacher.models.Teacher;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@PreAuthorize("hasAnyRole('ROLE_TEACHER', 'ROLE_STUDENT')")
@Slf4j
public class SettingsController {

    private final TeacherService teacherService;
    private final StudentService studentService;
    private final ModelMapper modelMapper;
    private final P_R_User pRUser;
    private static Student student;
    private static Teacher teacher;
    private String username = "";


    @Autowired
    public SettingsController(TeacherService teacherService, StudentService studentService, ModelMapper modelMapper, P_R_User pRUser) {
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.modelMapper = modelMapper;
        this.pRUser = pRUser;
    }

    @GetMapping("/settings")
    public String settings(Authentication authentication,
                           Principal principal,
                           Model model) {

        username = principal.getName();
        teacher = teacherService.findByUsername(username);
        student = studentService.findByUsername(username);

        log.info("Username -> {}", username);
        if (pRUser.isStudent(authentication)) {
            student_settings(model, studentService);
            return "student/settings";
        }

        teacher_settings(model, teacherService);
        return "teacher/settings";
    }
    @PostMapping("/settings/info")
    public String settings_info_student(Authentication authentication,
                                        @Valid @ModelAttribute("person_dto") PersonDTO personDTO,
                                        BindingResult result,
                                        @ModelAttribute("student") Student th_student,
                                        @ModelAttribute("photo_edit") Photo_Edit photoEdit,
                                        @ModelAttribute("password_edit") Password_Edit password_edit,
                                        Model model) {

        if (pRUser.isStudent(authentication)) {

            pRUser.validation_settings(personDTO, studentDTOConverter(student), result);

            if (result.hasErrors()) {
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

    @PostMapping("/settings/edit_photo")
    public String settings_edit_photo_student(Authentication authentication,
                                              @ModelAttribute("edit_photo") Photo_Edit photoEdit,
                                              Model model) {

        student.setPhoto(photoEdit.getEdit_photo());
        studentService.simple_save(student);

        return "redirect:/settings";
    }

    @PostMapping("/settings/edit_password")
    public String settings_edit_password_student(Authentication authentication,
                                                 @Valid @ModelAttribute("password_edit") Password_Edit password_edit,
                                                 BindingResult result,
                                                 @Valid @ModelAttribute("person_dto") PersonDTO personDTO,
                                                 @ModelAttribute("student") Student th_student,
                                                 @ModelAttribute("photo_edit") Photo_Edit photoEdit,
                                                 Model model) {


        String enc = student.getPassword();
        String raw = password_edit.getOld_password();
        boolean old_p_matches = pRUser.isPasswordMatch(raw, enc);


        if (!old_p_matches) {
            result.rejectValue("old_password", "","Old Password is Wrong !");
        }

        if (!password_edit.getNew_password().equals(password_edit.getRepeat_password())) {
            result.rejectValue("repeat_password", "","With New password does not matches !");
        }

        if (result.hasErrors()) {
            return "student/settings";
        }

        student.setPassword(password_edit.getNew_password());
        studentService.save(student);

        log.info("Passed !");
        return "redirect:/settings";
    }



    private Teacher complete_teacher(PersonDTO personDTO) {
        Teacher new_one = teacherConverter(personDTO);
        new_one.setRoles_teacher(teacher.getRoles_teacher());
        new_one.setTeacher_sections(teacher.getTeacher_sections());
        new_one.setStudents(teacher.getStudents());
        return new_one;
    }

    private Student complete_student(PersonDTO personDTO) {
        Student new_one = studentConverter(personDTO);
        new_one.setRoles_student(student.getRoles_student());
        new_one.setStudent_sections(student.getStudent_sections());
        new_one.setTeachers(student.getTeachers());
        return new_one;
    }


    void student_settings(Model model,
                          StudentService studentService) {
        model.addAttribute("student" , student);
        model.addAttribute("person_dto" , studentDTOConverter(student));
        model.addAttribute("photo_edit" , new Photo_Edit(student.getPhoto()));
        model.addAttribute("password_edit", new Password_Edit());

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


@AllArgsConstructor @NoArgsConstructor @Setter @Getter
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
