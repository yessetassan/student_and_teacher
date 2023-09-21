package com.example.student_and_teacher.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebPathConfigure implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/resource/**")
                .addResourceLocations("file:C:/Users/acer/Desktop/",
                        "file:C:/Users/acer/Downloads/",
                        "file:C:/Users/acer/Pictures/");
    }
}
