package com.example.student_and_teacher.controllers;


import com.example.student_and_teacher.models.FeedBack;
import com.example.student_and_teacher.services.FeedBackService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@PreAuthorize("hasAnyRole('ROLE_TEACHER', 'ROLE_STUDENT')")
public class FeedBackController {

    private final FeedBackService feedBackService;

    @Autowired
    public FeedBackController(FeedBackService feedBackService) {
        this.feedBackService = feedBackService;
    }

    @GetMapping("/feedback")
    public String feedback(@ModelAttribute("feedback")FeedBack feedBack) {
        return "feedback";
    }
    @PostMapping("/feedback")
    public String feedback_post(Principal principal,
                           @ModelAttribute("feedback")FeedBack feedBack) {
        feedBack.setUsername(principal.getName());
        feedBackService.save(feedBack);
        return "feedback_back";
    }





}
