package com.example.student_and_teacher.services;


import com.example.student_and_teacher.models.Course;
import com.example.student_and_teacher.repo.CourseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional
public class CourseService {

    private final CourseRepo courseRepo;

    @Autowired
    public CourseService(CourseRepo courseRepo) {
        this.courseRepo = courseRepo;
    }

    public void save(Course course) {
        courseRepo.save(course);
    }

    public Course findByCode(String code) {
        return courseRepo.findByCode(code);
    }
}
