package com.example.PetProject.security.validation;

import com.example.PetProject.security.models.CrmUser;
import com.example.PetProject.security.models.User;
import com.example.PetProject.security.service.UserRepository;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Comparator;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, CrmUser> {

    private final UserRepository userRepository;

    public UniqueEmailValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {

    }

    @Override
    public boolean isValid(CrmUser crmUser, ConstraintValidatorContext context) {

        boolean isValid = true;


        // This is logic of validation email for updating User.
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            try {
                User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                if (crmUser.getEmail().equals(authenticatedUser.getEmail())) {
                    return true;
                }else {
                    isValid = userRepository.findAll().stream()
                            .filter(user -> !user.getEmail().equals(authenticatedUser.getEmail()))
                            .collect(Collectors.toList())
                            .stream().noneMatch(user -> user.getEmail().equals(authenticatedUser.getEmail()));
                }
            } catch (Exception e) {

            }
        }

        // Compare email from form with emails from DB.
        if (crmUser.getEmail() != null){
            isValid = userRepository.findAll().stream().noneMatch(user -> user.getEmail().equals(crmUser.getEmail()));
        }

        // If not valid, throws an exception on field.
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("email").addConstraintViolation();
        }

        return isValid;
    }
}
