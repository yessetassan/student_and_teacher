package com.example.student_and_teacher.services;


import com.example.student_and_teacher.models.Privacy;
import com.example.student_and_teacher.repo.PrivacyRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PrivacyService {

    private final PrivacyRepo privacyRepo;

    public PrivacyService(PrivacyRepo privacyRepo) {
        this.privacyRepo = privacyRepo;
    }

    public Privacy findByUsername(String username) {
        return privacyRepo.findByUsername(username);
    }
    public void save(Privacy privacy) {
        privacyRepo.save(privacy);
    }
}
