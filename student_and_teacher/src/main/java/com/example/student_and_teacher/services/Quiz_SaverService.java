package com.example.student_and_teacher.services;


import com.example.student_and_teacher.models.Quiz_Saver;
import com.example.student_and_teacher.repo.Quiz_SaverRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class Quiz_SaverService {

    private final Quiz_SaverRepo quizSaverRepo;
    @Autowired
    public Quiz_SaverService(Quiz_SaverRepo quizSaverRepo) {
        this.quizSaverRepo = quizSaverRepo;
    }

    public Integer attempt_count(Integer student_id, Integer quiz_id) {
        return quizSaverRepo.attempt_count(student_id, quiz_id);
    }

    public List<Quiz_Saver> all(Integer student_id, Integer quiz_id) {
        return  quizSaverRepo.all(student_id, quiz_id);
    }

    public void save(Quiz_Saver quizSaver) {
        quizSaverRepo.save(quizSaver);
    }
}
