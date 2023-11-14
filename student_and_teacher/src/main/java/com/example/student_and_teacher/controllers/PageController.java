package com.example.student_and_teacher.controllers;


import com.example.student_and_teacher.models.*;
import com.example.student_and_teacher.services.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@PreAuthorize("hasAnyRole('ROLE_TEACHER', 'ROLE_STUDENT')")
@Slf4j
@RequestMapping("/dashboard")
public class PageController {

    private final TeacherService teacherService;
    private final StudentService studentService;
    private final SectionService sectionService;
    private final TaskService taskService;
    private final Quiz_ListService quizListService;
    private final QuizService quizService;
    private final Quiz_SaverService quizSaverService;
    private final Registration_ModeService registrationModeService;
    private final TaskSaverService taskSaverService;
    private String username = "";
    private Student student;
    private Teacher teacher;
    private Section section;
    private Boolean isStudent;
    private Boolean entered = false, ready_quiz = false;
    private Quiz quiz;
    private Task task;
    private TaskSaver taskSaver;
    private List<Quiz_List> quizListList = new ArrayList<>();
    private Map<Integer, List<Quiz_List>> map_question = new HashMap<>();


    @Autowired
    public PageController(TeacherService teacherService, StudentService studentService, SectionService sectionService, TaskService taskService, Quiz_ListService quizListService, QuizService quizService, Quiz_SaverService quizSaverService, Registration_ModeService registrationModeService, TaskSaverService taskSaverService) {
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.sectionService = sectionService;
        this.taskService = taskService;
        this.quizListService = quizListService;
        this.quizService = quizService;
        this.quizSaverService = quizSaverService;
        this.registrationModeService = registrationModeService;
        this.taskSaverService = taskSaverService;
    }

    @GetMapping("")
    public String dashboard2(@RequestParam(value = "readyQuiz", required = false) Boolean readyQuiz,
                             @RequestParam(value = "event_task", required = false) Integer event_task,
                             @RequestParam(value = "event_quiz", required = false) Integer event_quiz,
                             @RequestParam(value = "section_id", required = false) String section_id,
                            Authentication authentication,
                            Principal principal,
                            Model model) {

        username = principal.getName();
        student = studentService.findByUsername(username);
        teacher = teacherService.findByUsername(username);
        isStudent = isStudent(authentication);

        model.addAttribute("section",section);
        model.addAttribute("student", student);
        model.addAttribute("teacher", teacher);


        if (section_id != null) {
            section = sectionService.findId(Integer.valueOf(section_id));
            model.addAttribute("section",section);
            dashboard2_all(model);
            if (isStudent) return "student/dashboard2";
            else return "teacher/dashboard2";
        }
        else if (event_quiz != null) {
            quiz = quizService.findById(event_quiz);
            if (isStudent) {
                quiz_complete(model);
                return "student/quiz";
            }
            else {
                model.addAttribute("quiz", quiz);
                return "teacher/quiz";
            }
        }
        else if (event_task != null) {
            task = taskService.findById(event_task);
            model.addAttribute("untill" ,
                    date_form2(task.getCloses().toString()));
            if(isStudent){
                task_complete(model);
                return "student/dashboard3";
            }
            else {
                model.addAttribute("task", task);
                return "teacher/dashboard4";
            }

        }
        else if (readyQuiz != null) {
            ready_quiz = quiz.getAttempts() > quizSaverService.attempt_count(student.getId(),
                    quiz.getId());
            quiz_complete(model);
            if (ready_quiz) {
                if (isStudent) return "student/quiz2";
            }
            else{
                if (isStudent) return "student/quiz";
            }
        }

        if (isStudent) {
            entered = registrationModeService.findByUsername(username).getEntered();
            model.addAttribute("entered",entered);
            student_dashboard(model);
            return "student/dashboard";
        }
        teacher_dashboard(model);
        return "teacher/dashboard";
    }

    @GetMapping("/assignment")
    public String assignment(Authentication authentication,
                            Principal principal,
                            Model model) {
        model.addAttribute("teacher", teacher);
        model.addAttribute("section", section);
        Map<Integer, List<Quiz_List>> map_quiz = new LinkedHashMap<>();
        for (Quiz_List q : quizListService.findBySectionId(section.getId())) {
            map_quiz.putIfAbsent(q.getTop_id(), new ArrayList<>());
            map_quiz.get(q.getTop_id()).add(q);
        }
        model.addAttribute("map_quiz",map_quiz);
        model.addAttribute("quiz_sample", new Quiz());
        model.addAttribute("task_sample",new Task());

        return "teacher/dashboard3";
    }

    @PostMapping("/assignment")
    public String made_quiz(@ModelAttribute("task_sample") Task task_sample,
                            @ModelAttribute("quiz_sample") Quiz quiz_sample,
                            @RequestParam(value = "quiz_list_id", required = false) Integer quiz_list_id) {

        if (quiz_sample.getName() != null) {
            quiz_sample.setCreated(LocalDateTime.now());
            quiz_sample.setSection_id(section.getId());
            quiz_sample.setQuiz_list_id(quiz_list_id);
            quizService.save(quiz_sample);
        }
        else if (task_sample.getMessage() != null) {
            task_sample.setPublished_time(LocalDateTime.now());
            task_sample.setSection_id(section.getId());
            taskService.save(task_sample);
        }
        return "redirect:/dashboard";
    }

    private void task_complete(Model model) {
        taskSaver = taskSaverService.findByTaskId(task.getId());
        model.addAttribute("task", task);
        model.addAttribute("posted_date" ,
                date_form2(task.getPublished_time().toString()));

        if (taskSaver != null) {
            model.addAttribute("submitted_date" ,
                    date_form2(taskSaver.getSubmitted().toString()));
        }
        model.addAttribute("taskSaver", taskSaver);
        model.addAttribute("stringNeeded", new StringNeeded());
    }


    private String date_form2(String s) {
        int month = Integer.parseInt(s.substring(5,7)),
                day = Integer.parseInt(s.substring(8,10)),
                year = Integer.parseInt(s.substring(0,4)),
                hour = Integer.parseInt(s.substring(11,13)),
                minute = Integer.parseInt(s.substring(14,16));
        boolean pm = hour > 12;
        if (pm) hour -= 12;
        return day + " " + month_form(month) + " " + year + ", " +
                (hour >= 10 ? hour : "0" + hour) + ":"+ (minute > 10 ? minute : "0" + minute) + " " + (pm? "PM" : "AM");
    }

    public void quiz_complete(Model model) {
        int attempts = quizSaverService.attempt_count(student.getId(),
                quiz.getId());
        model.addAttribute("quiz", quiz);
        model.addAttribute("passed_attempts" , attempts);
        model.addAttribute("quiz_time", quiz.getTime_limit());
        model.addAttribute("quiz_lists", quizListService.findBySectionIdAndTopId(
                quiz.getSection_id(),
                quiz.getQuiz_list_id()
        ));
    }
    @PostMapping("/event_submit")
    public String event_submit(@ModelAttribute("stringNeeded") StringNeeded stringNeeded) {


        TaskSaver currentTaskSaver = taskSaverService.findByTaskIdStudentId(task.getId(),
                student.getId());
        if (currentTaskSaver == null) {
            currentTaskSaver = new TaskSaver(null,
                    task.getId(),
                    student.getId(),
                    stringNeeded.getTwo(),
                    LocalDateTime.now()
            );
            taskSaverService.save(currentTaskSaver);
        }
        else{
            currentTaskSaver.setFilePath(stringNeeded.getTwo());
            taskSaverService.save(currentTaskSaver);
        }
        return "redirect:/dashboard?event_task=" + task.getId();
    }
    @PostMapping("/event_delete")
    public String event_delete() {

        TaskSaver currentTaskSaver = taskSaverService.findByTaskIdStudentId(task.getId(),
                student.getId());
        taskSaverService.delete(currentTaskSaver);

        return "redirect:/dashboard?event_task=" + task.getId();
    }

    private void quizListList_save() {
        int top_id = quizListService.findQuizCount();

        for(Quiz_List q : quizListList) {
            q.setTop_id(top_id);
            quizListService.save(q);
        }
        quizListList = new ArrayList<>();
    }

    public void post_task_teacher(Task task) {
        // TODO

    }


    void student_dashboard(Model model) {
        model.addAttribute("student" , student);
        Set<Section> sections = sectionService.findAll().stream().filter(x ->
            x.getStudents().contains(student)
        ).collect(Collectors.toSet());
        model.addAttribute("sections",sections);
    }
    void dashboard2_all(Model model) {
        List<Task> tasks = taskService.all(section.getId());
        List<Quiz> quizzes = quizService.findBySectionId(section.getId());
        Map<String,List<Task>> map_tasks = new HashMap<>();
        Map<String, List<Quiz>> map_quizzes = new HashMap<>();
        TreeMap<String, String> map_time = new TreeMap<>(String::compareTo);
        fill_time(map_tasks,map_quizzes,map_time,tasks, quizzes);
        model.addAttribute("map_tasks", map_tasks);
        model.addAttribute("map_quizzes", map_quizzes);
        model.addAttribute("map_time", map_time);
    }

    private void fill_time(Map<String, List<Task>> map_tasks, Map<String, List<Quiz>> map_quizzes, Map<String,String> map_time, List<Task> tasks, List<Quiz> quizzes) {
        for (Task t : tasks) {
            map_tasks.putIfAbsent(t.getPublished_time().toString().substring(0,10),new ArrayList<>());
            map_tasks.get(t.getPublished_time().toString().substring(0,10)).add(t);
        }
        for (Quiz q : quizzes) {
            map_quizzes.putIfAbsent(q.getCreated().toString().substring(0,10),new ArrayList<>());
            map_quizzes.get(q.getCreated().toString().substring(0,10)).add(q);
        }
        map_tasks.forEach((x,y) -> map_time.put(x, date_form(x)));
        map_quizzes.forEach((x,y) -> map_time.put(x, date_form(x)));
    }

    private String date_form(String s) {
        int month = Integer.parseInt(s.substring(5,7)), day = Integer.parseInt(s.substring(8));
        return s.substring(0,4) + " year , " + day + " " + month_form(month);
    }

    private String month_form(int month) {
        return switch (month) {
            case 1 -> "January";
            case 2 -> "February";
            case 3 -> "March";
            case 4 -> "April";
            case 5 -> "May";
            case 6 -> "June";
            case 7 -> "July";
            case 8 -> "August";
            case 9 -> "September";
            case 10 -> "October";
            case 11 -> "November";
            default -> "December";
        };
    }

    void teacher_dashboard(Model model) {
        List<Section> sections = sectionService.findAll().stream().filter(x ->
                x.getTeacher() != null && x.getTeacher().hashCode() == teacher.hashCode()
        ).toList();
        model.addAttribute("sections",sections);
    }


    private boolean isStudent(Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch(
                x -> x.getAuthority().equals("ROLE_STUDENT")
        );
    }
}
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @ToString
class StringNeeded {
    private String one;
    private String two;
}
