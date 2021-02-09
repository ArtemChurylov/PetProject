package com.example.PetProject.application.course;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Controller
@RequestMapping("/course")
public class CourseController {

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
