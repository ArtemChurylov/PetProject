package com.example.PetProject.application.course;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
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

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/add")
    public String addCoursePage(Course course) {
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
}
