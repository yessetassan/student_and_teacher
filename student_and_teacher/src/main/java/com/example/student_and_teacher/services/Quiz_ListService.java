package com.example.student_and_teacher.services;

import com.example.student_and_teacher.models.Quiz_List;
import com.example.student_and_teacher.repo.Quiz_ListRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service @Transactional
public class Quiz_ListService {

    private final Quiz_ListRepo quizListRepo;

    @Autowired
    public Quiz_ListService(Quiz_ListRepo quizListRepo) {
        this.quizListRepo = quizListRepo;
    }

    public void save(Quiz_List quizList) {
        quizListRepo.save(quizList);
    }

    public Integer findQuizCount() {
        return quizListRepo.findQuizCount();
    }

    public List<Quiz_List> all() {
        return quizListRepo.all();
    }



}
