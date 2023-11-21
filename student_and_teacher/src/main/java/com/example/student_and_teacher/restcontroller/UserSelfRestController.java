package com.example.student_and_teacher.restcontroller;


import com.example.student_and_teacher.models.Privacy;
import com.example.student_and_teacher.services.PrivacyService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserSelfRestController {

    private final PrivacyService privacyService;
    @Autowired
    public UserSelfRestController(PrivacyService privacyService) {
        this.privacyService = privacyService;
    }

    @PostMapping("/privacy")
    ResponseEntity<String> save_privacy(@RequestBody Map<String,String> map) {
        Privacy privacy = privacyService.findByUsername(map.remove("username"));
        privacy.setAccess_myself(Boolean.parseBoolean(map.remove("access_myself")));
        privacy.setAccess_course(Boolean.parseBoolean(map.remove("access_course")));
        privacyService.save(privacy);
        return ResponseEntity.ok().body("Saved Privacy !");
    }
}
