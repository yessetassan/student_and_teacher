package com.example.student_and_teacher.controllers;


import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class WelcomeController {
    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
