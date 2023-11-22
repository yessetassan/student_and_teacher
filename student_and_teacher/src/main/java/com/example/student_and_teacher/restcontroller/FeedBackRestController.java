package com.example.student_and_teacher.restcontroller;


import com.example.student_and_teacher.controllers.FeedBackController;
import com.example.student_and_teacher.models.FeedBack;
import com.example.student_and_teacher.services.FeedBackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/feedback")
@Slf4j
public class FeedBackRestController {

    private final FeedBackService feedBackService;

    @Autowired
    public FeedBackRestController(FeedBackService feedBackService) {
        this.feedBackService = feedBackService;
    }

    @PostMapping("")
    ResponseEntity<String> feedback(@RequestBody Map<String,String> map) {
        System.out.println(map);
        feedBackService.save(new FeedBack(null,
                map.get("username"),
                map.get("message")));
        return ResponseEntity.ok().body("Successfully Send !");
    }
}
