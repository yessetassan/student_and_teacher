package com.example.student_and_teacher.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class SectionTime {
    private String section_name;
    private Set<String> times;

    @Override
    public String toString() {
        return "SectionTime{" +
                "section_id=" + section_name +
                ", times=" + times +
                '}';
    }
}
