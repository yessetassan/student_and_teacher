package com.example.student_and_teacher.repo;

import com.example.student_and_teacher.models.Task;
import com.example.student_and_teacher.models.TaskSaver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface TaskSaverRepo extends JpaRepository<TaskSaver, Integer> {

    @Query("select t from TaskSaver t where t.task_id = ?1")
    TaskSaver findByTask_id(Integer task_id);

    @Query("select t from TaskSaver t where t.task_id = ?1 and t.student_id = ?2")
    TaskSaver findByTaskIdStudentId(Integer id, Integer id2);
}
