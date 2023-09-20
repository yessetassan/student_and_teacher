package com.example.student_and_teacher.dto;


import com.example.student_and_teacher.models.Role;
import com.example.student_and_teacher.models.Section;
import com.example.student_and_teacher.models.Task;
import com.example.student_and_teacher.models.Teacher;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class TeacherDTO {
    private Integer id;
    private String f_name;
    private String l_name;
    private String username;
    private String email;
    private LocalDate birth_year;
    private Set<Role> roles_teacher;
    private Set<Teacher> teachers;
    private String photo;
    private Section section;
    private Set<Task> tasks;
}
