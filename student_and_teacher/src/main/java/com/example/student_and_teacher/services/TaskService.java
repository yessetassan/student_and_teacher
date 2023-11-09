package com.example.student_and_teacher.services;

import com.example.student_and_teacher.models.Section;
import com.example.student_and_teacher.models.Student;
import com.example.student_and_teacher.models.Task;
import com.example.student_and_teacher.repo.TaskRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
@Slf4j
public class TaskService {

    private final TaskRepo taskRepo;
    @Autowired
    public TaskService(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    public void save(Task task) {
        taskRepo.save(task);
    }
    public List<Task> all(Integer section_id) {
        return taskRepo.findBySectionId(section_id);
    }

    public Task findById(Integer event) {
        return taskRepo.findById(event).get();
    }
}
