package com.example.student_and_teacher.restcontroller;


import com.example.student_and_teacher.models.*;
import com.example.student_and_teacher.services.CourseService;
import com.example.student_and_teacher.services.SectionService;
import com.example.student_and_teacher.services.TeacherService;
import com.example.student_and_teacher.services.TimeService;
import com.example.student_and_teacher.validation.P_R_User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class SectionController {
    private final SectionService sectionService;
    private final CourseService courseService;
    private final TeacherService teacherService;
    private final TimeService timeService;
    private final P_R_User pRUser;
    private Integer total_cnt = 0;

    @Autowired
    public SectionController(SectionService sectionService, CourseService courseService, TeacherService teacherService, TimeService timeService, P_R_User pRUser) {
        this.sectionService = sectionService;
        this.courseService = courseService;
        this.teacherService = teacherService;
        this.timeService = timeService;
        this.pRUser = pRUser;
    }

    // Posts

    @GetMapping("/all_sections")
    public ResponseEntity<List<Section>> all() {

        return ResponseEntity.ok().body(sectionService.findAll());
    }
    @PostMapping("/add_section")
    public ResponseEntity<Object> add_section(@RequestBody Section section) {

        section.setCourse(section.getCourse());
        String exception = handle_exceptions(section);

        if(!exception.equals("ok")) {
            return ResponseEntity.badRequest().body(exception);
        }

        section = create_section_with_course(section);

        sectionService.save(section);

        return ResponseEntity.ok().body(section);
    }

    @PostMapping("/add_times_to_section")
    public ResponseEntity<Object> add_times_to_section(@RequestBody SectionTime sectionTime) {

        if (sectionTime.getSection_name() == null) {
            return ResponseEntity.badRequest().body("Section id not found !");
        }

        Section section = sectionService.findByName(sectionTime.getSection_name());

        if (section == null) {
            return ResponseEntity.badRequest().body("Section id not found !");
        }
        if (!pRUser.checkTimes(sectionTime.getTimes())) {
            return ResponseEntity.badRequest().body("Times not found !");
        }
        if (!pRUser.checkIntersectTimes(section , sectionTime.getTimes())) {
            return ResponseEntity.badRequest().body("Times are intersect  !");
        }

        Set<Time> timeSet = new HashSet<>();
        for (String s : sectionTime.getTimes()) {
            timeSet.add(timeService.findByName(s));
        }
        section.setTimes(timeSet);
        sectionService.save(section);

        return ResponseEntity.ok().body(sectionTime);
    }
    @PostMapping("/add_teacher_to_section/teacher_id/{t_id}/section_id/{s_id}")
    public ResponseEntity<Object> add_Teacher(@PathVariable("t_id") Integer t_id,
                                              @PathVariable("s_id") Integer s_id) {
        Teacher teacher = teacherService.findId(t_id);
        Section section = sectionService.findId(s_id);

        if (teacher == null || section == null) {
            return ResponseEntity.badRequest().body("Teacher or Section not found !");
        }

        section.setTeacher(teacher);

        sectionService.save(section);

        return ResponseEntity.ok().body(teacher);
    }

    @PostMapping("/section_update")
    public ResponseEntity<String> section_update() {

        List<Section> all = sectionService.findAll();

        for (Section s : all) {

            if (s.getRelated_section_name() != null) continue;

            s.setTotal_quota(0);
            all.stream().filter(x -> x.getRelated_section_name() != null && x.getRelated_section_name().equals(s.getName())).
                    forEach(x -> s.setTotal_quota(s.getTotal_quota() + x.getTotal_quota()));
            sectionService.save(s);
        }
        return ResponseEntity.ok().body("Passed !");
    }



    private String handle_exceptions(Section section) {

        if (section.getCourse() == null || courseService.findByCode(section.getCourse().getCode()) == null) {
            return "Course not found !";
        }
        Course course = courseService.findByCode(section.getCourse().getCode());
        section.setCourse(course);
        if (section.getType().equals("P")) {

            if (section.getRelated_section_name() == null) {
                return "Practice must have related_section !";
            }
            if (sectionService.findByName(section.getRelated_section_name()) == null) {
                return String.format("We don't have a such section like -> %s" , section.getRelated_section_name());
            }
        }

        return "ok";
    }

    private Section create_section_with_course(Section section) {

        Course course = section.getCourse();

        total_cnt = 1;
        sectionService.findAll().stream()
                .filter(x -> x.getCourse().equals(course))
                .forEach(x -> count());
        String  index = ".".concat(index(total_cnt));

        section.setName(course.getCode() + index);

        return section;
    }

    // Add Drop

    private String index(Integer totalCnt) {
        return totalCnt > 9 ? String.valueOf(totalCnt) : String.valueOf(0+""+totalCnt);
    }
    private void count() {
        total_cnt++;
    }
}
