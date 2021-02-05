package com.example.PetProject.security.validation;

import com.example.PetProject.security.models.CrmUser;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ConfirmPasswordValidator implements ConstraintValidator<ConfirmPassword, CrmUser> {
    @Override
    public void initialize(ConfirmPassword constraintAnnotation) {

    }

    @Override
    public boolean isValid(CrmUser crmUser, ConstraintValidatorContext context) {

        boolean isValid = true;

        if (crmUser.getPassword() != null && crmUser.getPassword().length() >= 6
        && crmUser.getConfirmPassword() != null && crmUser.getConfirmPassword().length() >= 6) {
            isValid = crmUser.getPassword().equals(crmUser.getConfirmPassword());
        }

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("confirmPassword").addConstraintViolation();
        }

        return isValid;
    }
}
