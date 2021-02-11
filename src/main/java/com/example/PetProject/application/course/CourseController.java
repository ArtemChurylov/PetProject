package com.example.PetProject.application.course;

import com.example.PetProject.security.models.User;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
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

    public CourseController(CourseService courseService, CourseRepository courseRepository) {
        this.courseService = courseService;
        this.courseRepository = courseRepository;
    }

    @GetMapping("/add")
    public String addCoursePage(Course course, Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("name", user.getName());
        model.addAttribute("lastName", user.getLastName());
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
        return "course/details";
    }
}
