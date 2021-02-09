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

    @GetMapping("/home")
    public String homePage(Model model) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("name", user.getName());
        model.addAttribute("lastName", user.getLastName());
        model.addAttribute("courses", courseRepository.findAll());
        return "homePage";
    }

    @GetMapping("/details")
    public String details(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("email", user.getEmail());
        model.addAttribute("name", user.getName());
        model.addAttribute("lastName", user.getLastName());
        model.addAttribute("role", user.getRole());
        return "details";
    }
}
