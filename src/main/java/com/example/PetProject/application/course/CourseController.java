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


    // If forms will contains spaces at first or at the end, its will be removed.
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

    // Returns page for adding course
    @GetMapping("/add")
    public String addCoursePage(Course course, Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        return "course/addCoursePage";
    }

    // Processing adding the course
    @PostMapping("/add")
    public String addCourse(@Valid Course course,
                            BindingResult result,
                            @RequestParam("file") MultipartFile file,
                            Model model) {

        if (result.hasErrors()){
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user", user);
            return "course/addCoursePage";
        }

        courseService.saveCourse(file, course);

        return "redirect:/home";
    }

    // Takes id and shows separated course for editing
    @GetMapping("/edit/{id}")
    public String editCoursePage(@PathVariable Long id, Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);

        Course course = courseRepository.findById(id).orElseThrow();
        model.addAttribute("course", course);
        return "course/editPage";
    }

    // Processing editing the course
    @PostMapping("/edit/{id}")
    public String editCourse(@Valid Course course, BindingResult result,
                             @RequestParam("file") MultipartFile file,
                             @PathVariable Long id, Model model) {
        if (result.hasErrors()){
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user", user);
            return "course/addCoursePage";
        }

        courseService.editCourse(file, course, id);

        return "redirect:/home";
    }

    // Returns course details page
    @GetMapping("/details/{id}")
    public String courseDetails(@PathVariable Long id, Model model) {
        Course course = courseRepository.findById(id).orElseThrow();
        model.addAttribute("course", course);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);

        return "course/details";
    }

    // Course delete logic
    @Transactional
    @PostMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id) {
        Course course = courseRepository.findById(id).orElseThrow();
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Refreshing created courses for logged in user
        for (int i = 0; i < loggedInUser.getCreatedCourses().size(); i++) {
            if (loggedInUser.getCreatedCourses().get(i).getId().equals(course.getId())) {
                loggedInUser.getCreatedCourses().remove(i);
            }
        }
        courseRepository.deleteById(id);
        return "redirect:/course/created";
    }

    // Returns page with created courses by logged in user
    @GetMapping("/created")
    public String showCreatedCourses(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);

        return "course/createdCourses";
    }

    // Returns page with courses which logged in user has bought
    @GetMapping("/myCourses")
    public String myCourses(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);

        return "course/myCourses";
    }

    @Transactional
    @PostMapping("/buy/{course_id}/{user_id}")
    public String buyCourse(@PathVariable("course_id") Long course_id,
                            @PathVariable("user_id") Long user_id) {
        User user = userRepository.findById(user_id).orElseThrow();
        Course course = courseRepository.findById(course_id).orElseThrow();

        // If the user tries to buy course which he was create
        if (user.getCreatedCourses().stream().noneMatch(course1 -> course1.equals(course))) {
            // If the user tries to buy course which he was already bought
            if (user.getCourses().stream().noneMatch(course1 -> course1.equals(course))) {
                // If the user has less money than course cost
                if (user.getBalance() >= Double.parseDouble(course.getPrice())) {

                    // Buy course logic
                    user.setBalance(user.getBalance() - Double.parseDouble(course.getPrice()));
                    user.addCourse(course);
                    course.addUser(user);

                    User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    loggedInUser.setBalance(user.getBalance());
                    loggedInUser.setCourses(user.getCourses());

                    courseRepository.save(course);
                    userRepository.save(user);
                } else {
                    // Don`t know how to throw an exception on button, so this is a decision.
                    return "redirect:/moneyException";
                }
            } else {
                //The same reason
                return "redirect:/buyException";
            }
        }else {
            //That`s too
            return "redirect:/ownCourseException";
        }

        return "redirect:/home";
    }


}
