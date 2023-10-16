package com.example.student_and_teacher.services;


import com.example.student_and_teacher.models.Time;
import com.example.student_and_teacher.repo.TimeRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional
public class TimeService {

    private final TimeRepo timeRepo;

    public TimeService(TimeRepo timeRepo) {
        this.timeRepo = timeRepo;
    }

    // Gets
    public Time findByName(String name) {
        return timeRepo.findTimeByName(name);
    }



    // Posts
    public void  save(Time time) {
        timeRepo.save(time);
    }
}
