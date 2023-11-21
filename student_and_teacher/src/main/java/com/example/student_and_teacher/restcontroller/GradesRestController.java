package com.example.student_and_teacher.restcontroller;


import com.example.student_and_teacher.models.Grades;
import com.example.student_and_teacher.models.GradesSaver;
import com.example.student_and_teacher.services.GradesSaverService;
import com.example.student_and_teacher.services.GradesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.Map;

@RestController
@RequestMapping("/grades")
@Slf4j
public class GradesRestController {

    private final GradesSaverService gradesSaverService;
    private final GradesService gradesService;
    private DecimalFormat df = new DecimalFormat("0.00");
    @Autowired
    public GradesRestController(GradesSaverService gradesSaverService, GradesService gradesService) {
        this.gradesSaverService = gradesSaverService;
        this.gradesService = gradesService;
    }
    @PostMapping("/post")
    public ResponseEntity<String> good(@RequestBody Map<String, String> map) {

        Integer grades_id = Integer.parseInt(map.remove("grades_id"));
        Grades grade = gradesService.findById(grades_id);
        for (String s : map.keySet()) {
            int student_id = Integer.parseInt(s);
            GradesSaver g = gradesSaverService.findByGradesIdStudentId(grades_id, student_id);
            g.setUserScore(Double.parseDouble(map.get(s)));
            Double percent = (g.getUserScore() / grade.getScore()) * 100.00;
            percent = Double.parseDouble(df.format(percent).replace(",", "."));
            System.out.println(percent);
            g.setUserPercentage(percent);
            gradesSaverService.save(g);
        }
        return ResponseEntity.ok().body("Request is done !");
    }
}
