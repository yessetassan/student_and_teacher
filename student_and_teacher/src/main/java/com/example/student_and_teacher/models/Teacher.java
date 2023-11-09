package com.example.student_and_teacher.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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




    @ManyToMany
    @JoinTable(
            name = "teacher_student",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    Set<Student> students = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "teacher_role",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    Set<Role> roles_teacher = new HashSet<>();

    @Override
    public int hashCode() {
        return this != null ? id : 0;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", f_name='" + f_name + '\'' +
                ", l_name='" + l_name + '\'' +
                ", email='" + email + '\'' +
                ", birth_year=" + birth_year +
                ", username='" + username + '\'' +
                ", password='" + "********" + '\'' + // it's not a good practice to print out passwords
                ", photo='" + photo + '\'' +
                ", students=" + (students != null ? students.size() : 0) + " students" +
                ", roles_teacher=" + (roles_teacher != null ? roles_teacher.size() : 0) + " roles" +
                '}';
    }


}

