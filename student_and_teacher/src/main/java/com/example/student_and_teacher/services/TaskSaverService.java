package com.example.student_and_teacher.services;


import com.example.student_and_teacher.models.TaskSaver;
import com.example.student_and_teacher.repo.TaskSaverRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class TaskSaverService {

    private final TaskSaverRepo taskSaverRepo;

    @Autowired
    public TaskSaverService(TaskSaverRepo taskSaverRepo) {
        this.taskSaverRepo = taskSaverRepo;
    }

    public void save(TaskSaver taskSaver) {
        taskSaverRepo.save(taskSaver);
    }

    public TaskSaver findByTaskId(Integer task_id) {
        return taskSaverRepo.findByTask_id(task_id);
    }

    public TaskSaver findByTaskIdStudentId(Integer id, Integer id2) {
        return taskSaverRepo.findByTaskIdStudentId(id, id2);
    }

    public void delete(TaskSaver currentTaskSaver) {
        taskSaverRepo.delete(currentTaskSaver);
    }
}
