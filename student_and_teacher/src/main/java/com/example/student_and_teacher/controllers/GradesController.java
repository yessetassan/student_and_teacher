package com.example.student_and_teacher.controllers;

import com.example.student_and_teacher.models.*;
import com.example.student_and_teacher.services.*;
import com.example.student_and_teacher.validation.P_R_User;
import lombok.*;
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


@Controller
@PreAuthorize("hasAnyRole('ROLE_TEACHER', 'ROLE_STUDENT')")
@Slf4j
@RequestMapping("/grades")
public class GradesController {
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final Registration_ModeService registrationModeService;
    private final SectionService sectionService;
    private final GradesService gradesService;
    private final GradesSaverService gradesSaverService;
    private final QuizService quizService;

    private final P_R_User pRUser;
    private static Student student;
    private static Teacher teacher;
    private Section teacher_section;
    private Section student_section;
    private String username = "";
    private Boolean show_section = false,isStudent = false;
    private Map<Integer, Student> map_students = new HashMap<>();
    private List<Section> sections;

    @Autowired
    public GradesController(TeacherService teacherService, StudentService studentService, Registration_ModeService registrationModeService, ModelMapper modelMapper, P_R_User pRUser, CourseService courseService, QuizService quizService, SectionService sectionService, GradesService gradesService, GradesSaverService gradesSaverService) {
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.registrationModeService = registrationModeService;
        this.pRUser = pRUser;
        this.quizService = quizService;
        this.sectionService = sectionService;
        this.gradesService = gradesService;
        this.gradesSaverService = gradesSaverService;
    }

    @GetMapping("")
    public String grades(@RequestParam(value = "eventId", required = false) Integer eventId,
                         @RequestParam(value = "section_id", required = false) Integer section_id,
                         Authentication authentication,
                         Principal principal,
                         Model model) {
        username = principal.getName();
        teacher = teacherService.findByUsername(username);
        student = studentService.findByUsername(username);
        isStudent = isStudent(authentication);


        model.addAttribute("teacher", teacher);
        model.addAttribute("student", student);

        if (isStudent) {

            model.addAttribute("section", null);
            if (section_id != null) {
                student_section = sectionService.findId(section_id);
                List<Object[]> grades = gradesSaverService.findByStudentIdAndSectionId(student.getId(),
                        section_id);
                model.addAttribute("section", student_section);
                model.addAttribute("grades", grades);
            }
            sections = student.getStudent_sections().stream().toList();
            model.addAttribute("sections",sections);

            return "student/grades";
        }

        if (eventId != null && teacher_section != null) {
            List<GradesSaver> list_grades_savers = gradesSaverService.findByGradesId(eventId);
            gradesSaverService.clear(list_grades_savers,teacher_section);
            list_grades_savers.forEach(x ->
                    map_students.put(x.getStudentId(), studentService.findById(x.getStudentId())));
            model.addAttribute("map_students",map_students);
            model.addAttribute("grades_saver",list_grades_savers);
            model.addAttribute("section", teacher_section);
            return "teacher/grades3";
        }
        if (section_id != null) {
            teacher_section = sectionService.findId(section_id);
            if (teacher_section.getTeacher().equals(teacher)) {
                model.addAttribute("section", teacher_section);
                model.addAttribute("grade", new Grades());
                model.addAttribute("grades",gradesService.findBySectionId(teacher_section.getId()));
                return "teacher/grades2";
            }
        }
        teacher_section = null;

        sections = sectionService.all().stream().filter(x ->
                x.getTeacher() != null && Objects.equals(x.getTeacher().getId(), teacher.getId())).toList();

        model.addAttribute("sections",sections);

        return "teacher/grades";
    }



    @PostMapping("/{grades_post}")
    public String grade2(@ModelAttribute("map_grades") Map<Integer, List<GradesSaver>> map) {
        System.out.println(map);
        return "redirect:/grades";
    }

    @PostMapping("/submit_event")
    public String grade3(@ModelAttribute("grades_saver") List<GradesSaver> grades_saver) {
        System.out.println(grades_saver);

        return "redirect:/grades";
    }
    @PostMapping()
    public String post(@ModelAttribute("grade") Grades grade) {
        grade.setSectionId(teacher_section.getId());
        grade.setCourseId(teacher_section.getCourse().getId());
        gradesService.save(grade);

        List<Student> students = sectionService.findAllStudents(teacher_section.getId());
        students.forEach(x ->
                gradesSaverService.save(new GradesSaver(null,
                        grade.getId(),
                        x.getId(),
                        null,
                        null,
                        null
                        )));
        return "redirect:/grades";
    }

    private boolean isStudent(Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch(
                x -> x.getAuthority().equals("ROLE_STUDENT")
        );
    }

}


