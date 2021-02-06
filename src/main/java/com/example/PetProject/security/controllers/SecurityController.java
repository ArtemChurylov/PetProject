package com.example.PetProject.security.controllers;

import com.example.PetProject.security.models.CrmUser;
import com.example.PetProject.security.models.User;
import com.example.PetProject.security.service.UserRepository;
import com.example.PetProject.security.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import javax.validation.Valid;

@Controller
public class SecurityController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SecurityController(UserService userService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
    public String createUser(@Valid CrmUser crmUser, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "registrationPage";
        }

        userService.save(crmUser);

        return "redirect:/home";
    }

    @GetMapping("/update")
    public String updateUserPage(CrmUser crmUser, Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("email", user.getEmail());
        model.addAttribute("name", user.getName());
        model.addAttribute("lastName", user.getLastName());
        return "updateUserPage";
    }

    @PostMapping("/update")
    public String updateUser(@Valid CrmUser crmUser, BindingResult result) {

        if (result.hasErrors()) {
            return "updateUserPage";
        }

        userService.updateUser(crmUser);

        return "redirect:/home";
    }
}
