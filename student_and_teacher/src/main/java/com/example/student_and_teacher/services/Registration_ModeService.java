package com.example.student_and_teacher.services;


import com.example.student_and_teacher.models.Registration_Mode;
import com.example.student_and_teacher.repo.Registration_ModeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class Registration_ModeService {

    private final Registration_ModeRepo registrationModeRepo;

    @Autowired
    public Registration_ModeService(Registration_ModeRepo registrationModeRepo) {
        this.registrationModeRepo = registrationModeRepo;
    }

    public Registration_Mode findByUsername(String username) {
        return registrationModeRepo.findByUsername(username);
    }

    public List<Registration_Mode> all() {
        return registrationModeRepo.findAll();
    }

    public void save(Registration_Mode registrationMode) {
        registrationModeRepo.save(registrationMode);
    }
}
