package com.example.PetProject.security.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

// Custom validation for matching passwords

@Documented
@Constraint(validatedBy = ConfirmPasswordValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfirmPassword {

    String message() default "Passwords dose`nt match.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
