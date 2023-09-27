package com.example.student_and_teacher.services;

import com.example.student_and_teacher.models.FeedBack;
import com.example.student_and_teacher.repo.FeedBackRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional @Slf4j
public class FeedBackService {

    private final FeedBackRepo feedBackRepo;

    @Autowired
    public FeedBackService(FeedBackRepo feedBackRepo) {
        this.feedBackRepo = feedBackRepo;
    }

    public void  save(FeedBack feedBack) {
        feedBackRepo.save(feedBack);
    }
}
