package com.example.student_and_teacher.dto;


import com.example.student_and_teacher.models.Role;
import com.example.student_and_teacher.models.Section;
import com.example.student_and_teacher.models.Teacher;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class PersonDTO {
    private Integer id;
    private String f_name;
    private String l_name;
    private String username;
    private String email;
    private String password;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth_year;
    private String photo;
    @Override
    public String toString() {
        return photo;
    }
}
