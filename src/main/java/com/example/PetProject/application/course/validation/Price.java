package com.example.PetProject.application.course.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PriceValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Price {

    String message() default "Invalid type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
