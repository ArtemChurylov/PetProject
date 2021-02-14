package com.example.PetProject.application.course;

import com.example.PetProject.security.models.User;
import com.example.PetProject.security.service.UserRepository;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Controller
@RequestMapping("/course")
public class CourseController {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    private final CourseService courseService;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseController(CourseService courseService, CourseRepository courseRepository, UserRepository userRepository) {
        this.courseService = courseService;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/add")
    public String addCoursePage(Course course, Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("name", user.getName());
        model.addAttribute("lastName", user.getLastName());
        model.addAttribute("balance", user.getBalance());
        return "course/addCoursePage";
    }

    @PostMapping("/add")
    public String addCourse(@Valid Course course, BindingResult result, @RequestParam("file") MultipartFile file) {

        if (result.hasErrors()){
            return "course/addCoursePage";
        }

        courseService.saveCourse(file, course);

        return "redirect:/home";
    }

    @GetMapping("/details/{id}")
    public String courseDetails(@PathVariable Long id, Model model) {
        Course course = courseRepository.findById(id).orElseThrow();
        model.addAttribute("course", course);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("name", user.getName());
        model.addAttribute("lastName", user.getLastName());
        model.addAttribute("balance", user.getBalance());
        model.addAttribute("user_id", user.getId());
        return "course/details";
    }

    @GetMapping("/myCourses")
    public String myCourses(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("courses", user.getCourses());
        model.addAttribute("name", user.getName());
        model.addAttribute("lastName", user.getLastName());
        model.addAttribute("balance", user.getBalance());
        return "course/myCourses";
    }

    @Transactional
    @PostMapping("/buy/{course_id}/{user_id}")
    public String buyCourse(@PathVariable("course_id") Long course_id,
                            @PathVariable("user_id") Long user_id) {
        User user = userRepository.findById(user_id).orElseThrow();
        Course course = courseRepository.findById(course_id).orElseThrow();


        if (user.getCourses().stream().noneMatch(course1 -> course1.equals(course))) {
            if (user.getBalance() >= Double.parseDouble(course.getPrice())) {
                user.setBalance(user.getBalance() - Double.parseDouble(course.getPrice()));

                user.addCourse(course);
                course.addUser(user);
//                List<User> userList = course.getUsers();
//                userList.add(user);
//                course.setUsers(userList);
                User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                loggedInUser.setBalance(user.getBalance());
                loggedInUser.setCourses(user.getCourses());

                courseRepository.save(course);
                userRepository.save(user);
            } else {
//                throw new IllegalStateException("Not enough money");
                return "redirect:/moneyException";
            }
        } else {
//            throw new IllegalStateException("You have already bought this course");
            return "redirect:/buyException";
        }
        return "redirect:/home";
    }


}
