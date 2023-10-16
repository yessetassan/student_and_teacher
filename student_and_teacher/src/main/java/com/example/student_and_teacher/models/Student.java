package com.example.student_and_teacher.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "student")
@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class Student implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            nullable = false,
            unique = true,
            updatable = false
    )
    private Integer id;

    @Column(
            name = "f_name",
            nullable = false
    )
    private String f_name;

    @Column(
            name = "l_name",
            nullable = false
    )
    private String l_name;

    @Column(
            name = "email",
            nullable = false,
            unique = true
    )
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(
            name = "birth_year",
            nullable = false
    )
    private LocalDate birth_year;

    @Column(
            name = "username",
            nullable = false
    )

    private String username;
    @Column(
            name = "password",
            nullable = false
    )
    private String password;

    @Column(
            name = "photo",
            nullable = false
    )
    private String photo = "User.jpg";

    @ManyToMany(mappedBy = "students")
    @JsonIgnore
    Set<Teacher> teachers = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "student_role",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonIgnore
    Set<Role> roles_student = new HashSet<>();


    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinTable(
            name = "student_section",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "section_id")
    )
    private Set<Section> student_sections;

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", f_name='" + f_name + '\'' +
                ", l_name='" + l_name + '\'' +
                ", email='" + email + '\'' +
                ", birth_year=" + birth_year +
                ", username='" + username + '\'' +
                ", password='" + "********" + '\'' +  // it's not a good practice to print out passwords
                ", photo='" + photo + '\'' +
                ", teachers=" + (teachers != null ? teachers.size() : 0) + " teachers" +
                ", roles_student=" + (roles_student != null ? roles_student.size() : 0) + " roles" +
                ", student_sections=" + (student_sections != null ? student_sections.size() : 0) + " sections" +
                '}';
    }

}
