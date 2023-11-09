package com.example.student_and_teacher.restcontroller;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.util.*;

@Controller
public class TestController {


    Map<String, List<String>> questionList = new HashMap<>();
    Map<String,String> replyList = new HashMap<>();

    @GetMapping("/quiz/all")
    public String quizPage(Model model) {
        // Assuming 'questionList' is defined in your application
        questionList.put("What's the capital of Qasaqstan?", Arrays.asList("Astana", "Abu-Dabi", "London", "Almaty"));
        questionList.put("What is 2 + 2?", Arrays.asList("3", "4", "5", "6"));
        questionList.put("Which planet is known as the Red Planet?", Arrays.asList("Mars", "Venus", "Jupiter", "Earth"));
        questionList.put("What is the largest mammal in the world?", Arrays.asList("Giraffe", "Elephant", "Blue Whale", "Lion"));


        // Add 'questionList' to the model
        model.addAttribute("questionList", questionList);
        model.addAttribute("replyList", replyList);

        return "test";
    }




}

