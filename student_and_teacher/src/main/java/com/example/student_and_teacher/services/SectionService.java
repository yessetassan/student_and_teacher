package com.example.student_and_teacher.services;


import com.example.student_and_teacher.models.Section;
import com.example.student_and_teacher.repo.SectionRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @Transactional @Slf4j
public class SectionService {

    private final SectionRepo sectionRepo;

    public SectionService(SectionRepo sectionRepo) {
        this.sectionRepo = sectionRepo;
    }

    public Section findId(Integer id) {
        return sectionRepo.findId(id);
    }

    public List<Section> findAll() {
        return sectionRepo.findAll();
    }
    public void save(Section section) {
     sectionRepo.save(section);
    }
    public Section findByName(String name) {
        return sectionRepo.findByName(name);
    }
}
