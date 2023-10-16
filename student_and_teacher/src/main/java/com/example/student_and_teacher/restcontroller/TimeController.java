package com.example.student_and_teacher.restcontroller;


import com.example.student_and_teacher.models.Time;
import com.example.student_and_teacher.services.TimeService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Component @AllArgsConstructor @NoArgsConstructor
public class TimeController {

    @Autowired
    private TimeService timeService;

    @GetMapping("/time")
    public void time() {

        for (int i = 1; i < 7; i++) {
            for (int j = 9; j < 19; j++) {

                String name = i + "." + j + ":00";
                if (j < 10) name = name.replace("" + j, "0" + j);
                Time time = new Time(name);
                timeService.save(time);
            }
        }
    }
}
