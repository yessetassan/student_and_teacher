package com.example.student_and_teacher.services;


import com.example.student_and_teacher.repo.GradesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional
public class GradesService {

    private final GradesRepo gradesRepo;

    @Autowired
    public GradesService(GradesRepo gradesRepo) {
        this.gradesRepo = gradesRepo;
    }

}
