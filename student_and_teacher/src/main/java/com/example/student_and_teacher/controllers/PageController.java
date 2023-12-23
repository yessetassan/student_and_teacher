package com.example.student_and_teacher.controllers;


import com.example.student_and_teacher.models.*;
import com.example.student_and_teacher.services.*;
import com.example.student_and_teacher.validation.P_R_User;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Duration;
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
    private final P_R_User p_r_user;
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
    public PageController(TeacherService teacherService, StudentService studentService, SectionService sectionService, TaskService taskService, Quiz_ListService quizListService, QuizService quizService, Quiz_SaverService quizSaverService, Registration_ModeService registrationModeService, TaskSaverService taskSaverService, P_R_User pRUser) {
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.sectionService = sectionService;
        this.taskService = taskService;
        this.quizListService = quizListService;
        this.quizService = quizService;
        this.quizSaverService = quizSaverService;
        this.registrationModeService = registrationModeService;
        this.taskSaverService = taskSaverService;
        p_r_user = pRUser;
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
            ready_quiz = check_join_quiz();
            quiz_complete(model);
            model.addAttribute("ready_quiz",ready_quiz);
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

    private Boolean check_join_quiz() {
        Duration diff1 = Duration.between(quiz.getOpens(),LocalDateTime.now()),
                diff2 = Duration.between(LocalDateTime.now(),quiz.getCloses());
        long d1 = diff1.toSeconds(), d2 = diff2.toSeconds();

        return d1 > 0 && d2 > 0 &&
        quiz.getAttempts() > quizSaverService.attempt_count(student.getId(),
                quiz.getId());
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
        taskSaver = taskSaverService.findByTaskIdStudentId(task.getId(),student.getId());
        model.addAttribute("task", task);
        model.addAttribute("posted_date" ,
                p_r_user.date_form2(task.getPublished_time().toString()));
        model.addAttribute("taskSaver", taskSaver);
        model.addAttribute("stringNeeded", new StringNeeded());
        String submitted_date = "Not Submitted at all";
        String time_remain = "You can submit it AnyTime !";
        if (task.getCloses() != null) {
            time_remain = diff_between_localdate(task.getCloses(),LocalDateTime.now());
            if (taskSaver != null && taskSaver.getSubmitted() != null) {
                submitted_date = diff_between_localdate(task.getCloses(), taskSaver.getSubmitted());
            }
        }
        submitted_date = submitted_date.replace("left", "early");
        model.addAttribute("time_remain", time_remain);
        model.addAttribute("submitted_date", submitted_date + " submission");

    }
    private String diff_between_localdate(LocalDateTime a, LocalDateTime b) {

        int compare = a.compareTo(b);
        Duration diff = Duration.between(b, a);
        long seconds = Math.abs(diff.toSeconds());
        long days = seconds / 86400,
                hours = (seconds % 86400) / 3600,
                minutes = (seconds % 3600) / 60;
        seconds %= 60;
        if (compare >= 0) {
            return (days > 0 ? days + " day(s), " : "") +  (hours > 0 ? hours + " hours, " : "") +
                    (minutes > 0 ? minutes + " minutes, " : "") + seconds + " seconds left ";
        }
        return (days > 0 ? days + " day(s), " : "") +  (hours > 0 ? hours + " hours, " : "") +
                (minutes > 0 ? minutes + " minutes, " : "") + seconds + " seconds late ";
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
        System.out.println(student);
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
        return s.substring(0,4) + " year , " + day + " " + p_r_user.month_form(month);
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
