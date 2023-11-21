package com.example.student_and_teacher.services;


import com.example.student_and_teacher.models.GradesSaver;
import com.example.student_and_teacher.models.Section;
import com.example.student_and_teacher.models.Student;
import com.example.student_and_teacher.repo.GradesSaverRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service @Transactional
public class GradesSaverService{

    private final GradesSaverRepo gradesSaverRepo;
    private final StudentService studentService;

    @Autowired
    public GradesSaverService(GradesSaverRepo gradesSaverRepo, StudentService studentService) {
        this.gradesSaverRepo = gradesSaverRepo;
        this.studentService = studentService;
    }

    public void  save(GradesSaver gradesSaver) {
        gradesSaverRepo.save(gradesSaver);
    }

    public List<GradesSaver> all() {
        return gradesSaverRepo.findAll();
    }

    public List<GradesSaver> findByGradesId(Integer eventId) {
        return gradesSaverRepo.findByGradesId(eventId);
    }

    public GradesSaver findByGradesIdStudentId(Integer gradesId, int studentId) {
        return gradesSaverRepo.findByGradesIdStudentId(gradesId, studentId);
    }
    public List<Object[]> findByStudentIdAndSectionId(Integer student_id, Integer section_id) {
        return gradesSaverRepo.findByStudentIdAndSectionId(student_id,section_id);
    }

    public void clear(List<GradesSaver> listGradesSavers, Section teacherSection) {
        for (GradesSaver g : listGradesSavers) {
            if (studentService.findSectionsByStudentId(g.getStudentId()).stream().noneMatch(x ->
                    Objects.equals(x.getId(), teacherSection.getId()))) {
                System.out.println(g.getStudentId() + " : " + teacherSection);
                gradesSaverRepo.deleteAll(gradesSaverRepo.findByStudentIdSectionId(g.getStudentId(),
                        teacherSection.getId()));
            }
        }
    }
}
