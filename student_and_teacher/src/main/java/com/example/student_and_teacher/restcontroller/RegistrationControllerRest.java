package com.example.student_and_teacher.restcontroller;


import com.example.student_and_teacher.models.Registration_Mode;
import com.example.student_and_teacher.services.Registration_ModeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registration_mode")
@Slf4j
public class RegistrationControllerRest {

    private final Registration_ModeService registrationModeService;

    @Autowired
    public RegistrationControllerRest(Registration_ModeService registrationModeService) {
        this.registrationModeService = registrationModeService;
    }

    @PostMapping("/add")
    public ResponseEntity<Registration_Mode> add_one(@RequestBody Registration_Mode registration_Mode) {
        registrationModeService.save(registration_Mode);
        return ResponseEntity.ok().body(registration_Mode);
    }
}
