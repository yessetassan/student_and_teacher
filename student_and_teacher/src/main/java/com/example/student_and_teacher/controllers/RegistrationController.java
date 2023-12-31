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
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@PreAuthorize("hasAnyRole('ROLE_TEACHER', 'ROLE_STUDENT')")
@Slf4j
@RequestMapping("/registration")
public class RegistrationController {
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final Registration_ModeService registrationModeService;
    private final ModelMapper modelMapper;
    private final P_R_User pRUser;
    private static Student student;
    private static Teacher teacher;
    private String username = "";
    private Boolean entered = false;
    private final CourseService courseService;
    private final SectionService sectionService;
    @Autowired
    public RegistrationController(TeacherService teacherService, StudentService studentService, Registration_ModeService registrationModeService, ModelMapper modelMapper, P_R_User pRUser, CourseService courseService, SectionService sectionService) {
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.registrationModeService = registrationModeService;
        this.modelMapper = modelMapper;
        this.pRUser = pRUser;
        this.courseService = courseService;
        this.sectionService = sectionService;
    }

    @GetMapping("")
    public String settings(@RequestParam(value = "search_code", required = false) String search_code,
                           @RequestParam(value = "course_code", required = false) String course_code,
                           Authentication authentication,
                           Principal principal,
                           Model model) {

        username = principal.getName();
        teacher = teacherService.findByUsername(username);
        student = studentService.findByUsername(username);
        entered = registrationModeService.findByUsername(username) != null ?
                registrationModeService.findByUsername(username).getEntered() : false;
        log.info("{} is logged", username);

        model.addAttribute("student", student);
        model.addAttribute("teacher", teacher);

        model.addAttribute("entered",entered);

        if (course_code != null) {
            Course course = courseService.findByCode(course_code);
            if (course != null)  {

                if (pRUser.isStudent(authentication)) {
                    System.out.println("Yeas sections are seeking out !" + course);
                    List<Section> list_sections = sectionService.findByCourseId(course.getId());
                    model.addAttribute("course", course);
                    model.addAttribute("list_sections", list_sections);
                    model.addAttribute("sections_exist", student.getStudent_sections().stream().allMatch(x ->
                            list_sections.stream().noneMatch(x::equals)));
                    return "student/registration2";
                }
            }
        }

        List<Course> courseList = courseService.all();
        if (search_code != null && search_code.length() > 0 && courseService.findByCode(search_code) != null) {
            courseList = new ArrayList<>(Collections.singletonList(courseService.findByCode(search_code)));
        }
        log.info("Username entered to Registration -> {}", username);
        if (pRUser.isStudent(authentication)) {
            model.addAttribute("courses", courseList);
            model.addAttribute("sections" , student.getStudent_sections());
            return "student/registration";
        }

        return "student/registration";
    }

    @PostMapping("/section_id")
    public String courseNamePost(@RequestParam("section_id") String section_id) {

        Set<Section> sectionSet = studentService.findSectionsByStudentUsername(username);
        Section practice = sectionService.findId(Integer.valueOf(section_id));
        Section lecture = sectionService.findByName(practice.getRelated_section_name());

        if (Objects.equals(practice.getCurrent_quota(), practice.getTotal_quota())) return "redirect:/registration";
        log.info("{} is section id !", section_id);
        practice.setCurrent_quota(practice.getCurrent_quota() + 1);
        lecture.setCurrent_quota(lecture.getCurrent_quota() + 1);
        if (Objects.equals(practice.getTotal_quota(),practice.getCurrent_quota())) practice.setAvailability(false);
        if (Objects.equals(lecture.getTotal_quota(),lecture.getCurrent_quota())) lecture.setAvailability(false);
        sectionSet.add(practice);
        sectionSet.add(lecture);
        sectionService.save(practice);
        sectionService.save(lecture);

        student.setStudent_sections(sectionSet);
        studentService.simple_save(student);
        return "redirect:/registration";
    }

    @GetMapping("/enter")
    public String add() {
        Registration_Mode registrationMode = registrationModeService.findByUsername(username);
        registrationMode.setEntered(true);
        registrationModeService.save(registrationMode);
        return "redirect:/registration";
    }

    @GetMapping("/nonenter")
    public String nonenter() {
        Registration_Mode registrationMode = registrationModeService.findByUsername(username);
        registrationMode.setEntered(false);
        registrationModeService.save(registrationMode);
        return "redirect:/registration";
    }

    @PostMapping("/drop")
    public String drop(@RequestParam("section_id") Integer id) {

        log.info("{} is ready to dropped !", id);
        Section practice = sectionService.findId(id),
                lec = sectionService.findByName(practice.getRelated_section_name());
        Set<Section> sectionSet = student.getStudent_sections();
        student.setStudent_sections(sectionSet.stream().filter(x ->
                !Objects.equals(x.getId(), id) &&
                        !Objects.equals(x.getId(), lec.getId())).collect(Collectors.toSet()));
        studentService.simple_save(student);
        practice.setCurrent_quota(practice.getCurrent_quota() - 1);
        lec.setCurrent_quota(lec.getCurrent_quota() - 1);
        practice.setAvailability(true);
        lec.setAvailability(true);
        sectionService.save(practice);
        sectionService.save(lec);
        return "redirect:/registration";
    }


}
