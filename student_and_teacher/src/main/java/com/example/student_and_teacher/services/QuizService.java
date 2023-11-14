package com.example.student_and_teacher.services;

import com.example.student_and_teacher.models.Quiz;
import com.example.student_and_teacher.repo.QuizRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class QuizService {

    private final QuizRepo quizRepo;

    @Autowired
    public QuizService(QuizRepo quizRepo) {
        this.quizRepo = quizRepo;
    }

    public void save(Quiz quiz) {
        quizRepo.save(quiz);
    }

    public List<Quiz> all() {
        return quizRepo.all();
    }

    public Quiz findById(Integer id) {
        return quizRepo.findById1(id);
    }

    public List<Quiz> findBySectionId(Integer id) {
        return quizRepo.findBySectionId(id);
    }

}