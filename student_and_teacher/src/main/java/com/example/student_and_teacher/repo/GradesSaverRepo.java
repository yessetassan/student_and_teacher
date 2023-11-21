package com.example.student_and_teacher.repo;

import com.example.student_and_teacher.models.GradesSaver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface GradesSaverRepo extends JpaRepository<GradesSaver, Integer> {
    @Query("select g from GradesSaver g where g.grades_id = ?1")
    List<GradesSaver> findByGradesId(Integer eventId);

    @Query("select g from GradesSaver g where g.grades_id = ?1 and g.studentId = ?2")
    GradesSaver findByGradesIdStudentId(Integer gradesId, int studentId);

    @Query("select g2.eventName, g2.score,g.userScore,g.userPercentage from GradesSaver g inner join Grades g2 " +
            "on g.grades_id = g2.id where g.studentId = ?1 and g2.sectionId = ?2")
    List<Object[]> findByStudentIdAndSectionId(Integer studentId, Integer sectionId);

    @Query("select g from GradesSaver g where g.studentId = ?1 and g.grades_id in (" +
            "select g2.id from Grades g2 where g2.sectionId = ?2)")
    List<GradesSaver> findByStudentIdSectionId(Integer studentId, Integer id);
}
