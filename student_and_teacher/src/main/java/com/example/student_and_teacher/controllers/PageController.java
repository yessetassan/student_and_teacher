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
    private Boolean entered = false;
    private Boolean readyQuiz;
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
    public String dashboard(@RequestParam(value = "event", required = false) Integer event,
                            @RequestParam(value = "section_id", required = false) String section_id,
                            Authentication authentication,
                            Principal principal,
                            Model model) {

        username = principal.getName();
        student = studentService.findByUsername(username);
        teacher = teacherService.findByUsername(username);
        log.info("{} is logged dashboard !", username);
        isStudent = isStudent(authentication);

        model.addAttribute("section",section);
        model.addAttribute("student", student);
        model.addAttribute("teacher", teacher);

        if (event != null && section != null) {

            task = taskService.findById(event);
            taskSaver = taskSaverService.findByTaskId(task.getId());
            task_complete(model);
            if (isStudent) return "student/dashboard3";
        }

        if (section_id != null) {
            section = sectionService.findId(Integer.valueOf(section_id));
            if (isStudent) {
                student_dashboard2(model);
                return "student/dashboard2";
            }
            else{
                teacher_dashboard2(model);
                return "teacher/dashboard2";
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

    private void task_complete(Model model) {

        if (task != null) {
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

    @GetMapping("/quiz/{quiz_id}")
    public String quiz(@RequestParam(value = "ready", required = false) Boolean ready,
                       @PathVariable("quiz_id") Integer quiz_id,
                       Model model) {

        log.info("Quiz1 is entered chat ! -> {}", username);
        quiz = quizService.findById(quiz_id);
        model.addAttribute("student", student);
        model.addAttribute("quiz", quiz);
        readyQuiz = false;


        if (ready != null) {
            readyQuiz = quiz.getAttempts() - quizSaverService.attempt_count(
                    student.getId(),
                    quiz_id
            )  > 0;
        }

        if (readyQuiz) {
            log.info("Quiz2 is entered chat ! -> {}", username);
            model.addAttribute("quiz_time" , 5);
            quiz_time(model);
            return "student/quiz2";
        }

        model.addAttribute("quiz", quizService.findById(quiz_id));
        model.addAttribute("section", section);
        if (isStudent) {
            List<Quiz_Saver> quiz_savers = quizSaverService.all(student.getId(), quiz_id);
            quiz_complete(model, quiz_savers);
            System.out.println(quiz_savers);
            return "student/quiz";
        }
        model.addAttribute("teacher", teacher);

        return "teacher/quiz";
    }

    @PostMapping("/quiz")
    public String quiz_submit() {
        log.info("{} quiz is redirected", quiz.getId());
        return "redirect:/dashboard/quiz/" + quiz.getId();
    }

    private void quiz_time(Model model) {
        Set<Quiz_List> quiz_lists = quizListService.all().stream().filter( x->
                x.getTop_id() == quiz.getQuiz_list_id()).collect(Collectors.toSet());
        model.addAttribute("quiz_lists",quiz_lists);

    }

    public void quiz_complete(Model model, List<Quiz_Saver> quiz_savers) {
        Map<Integer, List<Quiz_Saver>> map_quiz_saver = new HashMap<>();
        for (Quiz_Saver q : quiz_savers) {
            if (map_quiz_saver.containsKey(q.getAttempt_order())) {
                map_quiz_saver.get(q.getAttempt_order()).add(q);
            }
            else map_quiz_saver.put(q.getAttempt_order(), new ArrayList<>(Arrays.asList(q)));
        }
        model.addAttribute("map_quiz_saver", map_quiz_saver);
    }


    // This method only for Teachers...
    @GetMapping("/set_up")
    public String dashboard2(Model model) {
        map_question = new HashMap<>();
        teacher_dashboard3(model);
        return "teacher/dashboard3";
    }

    @PostMapping("/set_up")
    public String dashboard_post(@ModelAttribute("task") Task task,
                                 @ModelAttribute("quiz_list") Quiz_List quizList,
                                 @ModelAttribute("quiz") Quiz quiz,
                                 @RequestParam(value = "delete", required = false) String delete,
                                 @RequestParam(value = "continue", required = false) Boolean to_be_continued,
                                 @RequestParam(value = "option_a", required = false) String option_a,
                                 @RequestParam(value = "option_b", required = false) String option_b,
                                 @RequestParam(value = "option_c", required = false) String option_c,
                                 @RequestParam(value = "option_d", required = false) String option_d
                                 ) {

        if (delete != null) {
            quizListList = quizListList.stream().filter(x ->
                    !x.getQuestion().equals(delete)).collect(Collectors.toList());
            return "redirect:/dashboard/set_up";
        }
        else if (task.getMessage() != null) {
            post_task_teacher(task);
        }
        else if (quizList.getQuestion() != null) {
            quizList.setOptions(option_a + "|" + option_b + "|" + option_c + "|" + option_d);
            quizListList.add(quizList);
            if (to_be_continued) {
                return "redirect:/dashboard/set_up";
            }
            else{
                quizListList_save();
            }
        }
        else if (quiz.getName() != null) {
            quiz.setSection_id(section.getId());
            quiz.setCreated(LocalDateTime.now());
            quizService.save(quiz);
        }

        return "redirect:/dashboard";
    }

    @PostMapping("/event_submit")
    public String event_submit(@ModelAttribute("stringNeeded") StringNeeded stringNeeded) {

        System.out.println("file_location ->  " + stringNeeded);

        TaskSaver currentTaskSaver = taskSaverService.findByTaskIdStudentId(task.getId(),
                student.getId());
        if (currentTaskSaver == null) {
            currentTaskSaver = new TaskSaver(null,
                    task.getId(),
                    student.getId(),
                    stringNeeded.getOne(),
                    LocalDateTime.now()
            );
            taskSaverService.save(currentTaskSaver);
        }
        else{
            currentTaskSaver.setFilePath(stringNeeded.getTwo());
            taskSaverService.save(currentTaskSaver);
        }
        return "redirect:/dashboard?event=" + task.getId();
    }
    @PostMapping("/event_delete")
    public String event_delete() {


        TaskSaver currentTaskSaver = taskSaverService.findByTaskIdStudentId(task.getId(),
                student.getId());
        taskSaverService.delete(currentTaskSaver);

        return "redirect:/dashboard?event=" + task.getId();
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
    void student_dashboard2(Model model) {
        List<Task> tasks = taskService.all(section.getId());
        Map<String,List<Task>> map_task = new HashMap<>();
        Map<String,String> map_time = new HashMap<>();
        fill_time(map_task,map_time,tasks);
        model.addAttribute("student" , student);
        model.addAttribute("section",section);
        model.addAttribute("map_task", map_task);
        model.addAttribute("map_time", map_time);
    }

    private void fill_time(Map<String, List<Task>> map_task, Map<String,String> map_time, List<Task> tasks) {
        for (Task t : tasks) {
            map_task.putIfAbsent(t.getPublished_time().toString().substring(0,10),new ArrayList<>());
            map_task.get(t.getPublished_time().toString().substring(0,10)).add(t);
        }
        for (String s : map_task.keySet()) {
            map_time.put(s, date_form(s));
        }
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
        model.addAttribute("teacher" , teacher);
        Set<Section> sections = sectionService.findAll().stream().filter(x ->
                x.getTeacher() != null && x.getTeacher().hashCode() == teacher.hashCode()
        ).collect(Collectors.toSet());
        model.addAttribute("sections",sections);
    }

    void teacher_dashboard2(Model model) {
        // ToDo
//        model.addAttribute("teacher" , teacher);
//        Set<Task> tasks = sectionService.findTasksById(section.getId());
//        model.addAttribute("section",section);
//        model.addAttribute("tasks", tasks);
//        model.addAttribute("quizzes", quizService.all());
    }

    void teacher_dashboard3(Model model) {

        model.addAttribute("teacher" , teacher);
        model.addAttribute("task", new Task());
        model.addAttribute("section",section);
        model.addAttribute("quiz_list", new Quiz_List());
        model.addAttribute("quizListList",quizListList);

        List<Quiz_List> list = quizListService.all();
        for (Quiz_List l : list) {
            if (map_question.get(l.getTop_id()) != null) {
                map_question.get(l.getTop_id()).add(l);
            }
            else
                map_question.put(l.getTop_id(), new ArrayList<>(Arrays.asList(l)));
        }
        model.addAttribute("map_question",map_question);
        model.addAttribute("quiz", new Quiz());


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
