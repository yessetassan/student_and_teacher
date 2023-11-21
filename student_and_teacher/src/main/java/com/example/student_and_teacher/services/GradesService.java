package com.example.student_and_teacher.services;


import com.example.student_and_teacher.models.Grades;
import com.example.student_and_teacher.models.GradesSaver;
import com.example.student_and_teacher.repo.GradesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @Transactional
public class GradesService {

    private final GradesRepo gradesRepo;

    @Autowired
    public GradesService(GradesRepo gradesRepo) {
        this.gradesRepo = gradesRepo;
    }


    public void save(Grades grades) {
        gradesRepo.save(grades);
    }

    public List<Grades> all() {
        return gradesRepo.findAll();
    }

    public List<Grades> findBySectionId(Integer id) {
        return gradesRepo.findBySectionId(id);
    }
    public Grades findById(Integer id) {
        return gradesRepo.findById(id).get();
    }
}
