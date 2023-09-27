package com.example.student_and_teacher.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "teacher")
@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class Teacher implements Serializable {

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




    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "teacher_student",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    @JsonManagedReference
    Set<Student> students = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "teacher_role",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonManagedReference
    Set<Role> roles_teacher = new HashSet<>();


    @OneToMany(mappedBy = "teacher")
    @JsonBackReference
    private Set<Section> teacher_sections;

    @OneToMany
    @JsonBackReference
    private Set<Task> tasks = new HashSet<>();


}

