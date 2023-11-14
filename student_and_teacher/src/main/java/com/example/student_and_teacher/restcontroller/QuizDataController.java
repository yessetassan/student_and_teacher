package com.example.student_and_teacher.restcontroller;


import com.example.student_and_teacher.models.Quiz;
import com.example.student_and_teacher.models.Quiz_List;
import com.example.student_and_teacher.models.Quiz_Saver;
import com.example.student_and_teacher.models.Student;
import com.example.student_and_teacher.services.QuizService;
import com.example.student_and_teacher.services.Quiz_ListService;
import com.example.student_and_teacher.services.Quiz_SaverService;
import com.example.student_and_teacher.services.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Slf4j
public class QuizDataController {

    private final Quiz_SaverService quizSaverService;
    private final QuizService quizService;
    private final Quiz_ListService quizListService;
    private final StudentService studentService;
    @Autowired
    public QuizDataController(Quiz_SaverService quizSaverService, QuizService quizService, Quiz_ListService quizListService, StudentService studentService) {
        this.quizSaverService = quizSaverService;
        this.quizService = quizService;
        this.quizListService = quizListService;
        this.studentService = studentService;
    }
    @PostMapping("/saveQuizData")
    public ResponseEntity<String> saveQuizData(@RequestBody Map<String, String> quizData) {

        Student student = studentService.findByUsername(quizData.get("student_name"));
        int quiz_id = Integer.parseInt(quizData.get("quiz_id")),
                attempt = quizSaverService.attempt_count(student.getId(),quiz_id) + 1;
        Quiz quiz = quizService.findById(quiz_id);
        List<Quiz_List> quiz_lists = quizListService.all().stream().filter(x ->
                Objects.equals(x.getTop_id(), quiz.getQuiz_list_id())).toList();

        for (Quiz_List q : quiz_lists) {
            String question = q.getQuestion(),
                answer = quizData.get(question);
            Quiz_Saver quizSaver = new Quiz_Saver(null,
                    quiz_id,
                    q.getId(),
                    answer,
                    q.getRight_option().equals(answer) ? q.getScore() : 0,
                    attempt,
                    student.getId());
            System.out.println(quizSaver);
            quizSaverService.save(quizSaver);
        }


        log.info("{} student  get passed ", student.getId());
        return   ResponseEntity.ok("Quiz Saved !");
    }

    @PostMapping("/saveQuizDataForSection")
    public ResponseEntity<String> saveQuizDataForSection(@RequestBody Map<String, List<String>> quiz_map) {

        int top_id = quizListService.findQuizCount(),
                section_id = Integer.parseInt(quiz_map.remove("section_id").get(0));
        quiz_map.forEach((key,value) ->
                quizListService.save(new Quiz_List(
                null,
                key,
                options_tag(value.subList(0,4)),
                value.get(4),
                Double.parseDouble(value.get(5)),
                top_id,
                section_id
        )));

        return   ResponseEntity.ok("Section Quiz Saved !");
    }

    private String options_tag(List<String> strings) {
        StringBuilder s = new StringBuilder();
        for (String w : strings) s.append(w).append("|");
        s.deleteCharAt(s.length() - 1);
        return s.toString();
    }



}
