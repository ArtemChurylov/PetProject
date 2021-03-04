package com.example.PetProject.security.controllers;

import com.example.PetProject.security.models.CrmUser;
import com.example.PetProject.security.models.User;
import com.example.PetProject.security.service.UserService;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class SecurityController {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    private final UserService userService;

    public SecurityController(UserService userService) {
        this.userService = userService;
    }

    // Returns login page
    @GetMapping("/login")
    public String login() {

        return "security/loginPage";
    }

    // Returns registration page
    @GetMapping("/registration")
    public String registration(CrmUser crmUser) {
        return "security/registrationPage";
    }

    //Provides registration of user
    @PostMapping("/registration")
    public String createUser(@Valid CrmUser crmUser, BindingResult result) {

        if (result.hasErrors()) {
            return "security/registrationPage";
        }

        userService.save(crmUser);

        return "redirect:/home";
    }

    // Returns update user page
    @GetMapping("/update")
    public String updateUserPage(CrmUser crmUser, Model model) {

        // Takes authenticated user from SecurityContext to show user details in form
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        return "security/updateUserPage";
    }

    //Provides update of user
    @PostMapping("/update")
    public String updateUser(@Valid CrmUser crmUser, BindingResult result, Model model) {

        if (result.hasErrors()) {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user", user);
            return "security/updateUserPage";
        }

        userService.updateUser(crmUser);

        return "redirect:/home";
    }
}
