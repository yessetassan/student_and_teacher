package com.example.student_and_teacher.impl;

import com.example.student_and_teacher.models.Student;
import com.example.student_and_teacher.models.Teacher;
import com.example.student_and_teacher.services.StudentService;
import com.example.student_and_teacher.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class PersonDetailsService implements UserDetailsService {

    private final StudentService studentService;
    private final TeacherService teacherService;

    @Autowired
    public PersonDetailsService(StudentService studentService, TeacherService teacherService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Student> student = studentService.findAll().stream().filter(
                x -> x.getUsername().equals(username)
        ).findAny();

        if (student.isPresent()) {
            return new StudentDetails(student.get());
        }

        Optional<Teacher> teacher = teacherService.findAll().stream().filter(
                x -> x.getUsername().equals(username)
        ).findAny();

        if (teacher.isPresent()) {
            return new TeacherDetails(teacher.get());
        }

        throw new UsernameNotFoundException("Username now found !");
    }
}
