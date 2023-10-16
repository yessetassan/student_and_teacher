package com.example.student_and_teacher;

import com.example.student_and_teacher.restcontroller.TimeController;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class StudentAndTeacherApplication {

	// This code will be added one time !

//	private final TimeController timeController;
//
//	@Autowired
//	public StudentAndTeacherApplication(TimeController timeController) {
//		this.timeController = timeController;
//	}

	//	@Bean
//	CommandLineRunner commandLineRunner() {
//		return args -> {
//			timeController.time();
//		};
//	}

	public static void main(String[] args) {
		SpringApplication.run(StudentAndTeacherApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}


}
