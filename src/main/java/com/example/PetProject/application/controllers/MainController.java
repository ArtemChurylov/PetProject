package com.example.PetProject.application.controllers;

import com.example.PetProject.application.course.CourseRepository;
import com.example.PetProject.security.models.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    private final CourseRepository courseRepository;

    public MainController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }


    // Returns home page
    @GetMapping("/home")
    public String homePage(Model model) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // To show user details in header
        model.addAttribute("user", user);
        // To show all courses on home page
        model.addAttribute("courses", courseRepository.findAll());
        return "homePage";
    }

    // Returns page with user details
    @GetMapping("/details")
    public String details(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        return "details";
    }

    @GetMapping("/moneyException")
    public String moneyException() {
        return "exceptions/moneyException";
    }

    @GetMapping("/buyException")
    public String buyException() {
        return "exceptions/buyException";
    }

    @GetMapping("/ownCourseException")
    public String ownCourseException() {
        return "exceptions/ownCourseException";
    }


}
