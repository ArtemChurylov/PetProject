package com.example.PetProject.security.validation;

import com.example.PetProject.security.models.CrmUser;
import com.example.PetProject.security.service.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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

        if (crmUser.getEmail() != null){
            isValid = userRepository.findAll().stream().noneMatch(user -> user.getEmail().equals(crmUser.getEmail()));
        }

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("email").addConstraintViolation();
        }

        return isValid;
    }
}
