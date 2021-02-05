package com.example.PetProject.security.controllers;

import com.example.PetProject.security.models.CrmUser;
import com.example.PetProject.security.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class SecurityController {

    private final UserService userService;

    public SecurityController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "loginPage";
    }

    @GetMapping("/registration")
    public String registration(CrmUser crmUser) {
        return "registrationPage";
    }

    @PostMapping("/registration")
    public String createUser(@Valid CrmUser crmUser, BindingResult result) {

        if (result.hasErrors()) {
            return "registrationPage";
        }

        userService.save(crmUser);

        return "redirect:/";
    }
}
