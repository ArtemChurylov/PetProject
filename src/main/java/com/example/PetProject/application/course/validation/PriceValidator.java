package com.example.PetProject.application.course.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PriceValidator implements ConstraintValidator<Price, Double> {
    @Override
    public void initialize(Price constraintAnnotation) {

    }

    @Override
    public boolean isValid(Double price, ConstraintValidatorContext context) {

        boolean isValid = true;

        if (String.valueOf(price).contains("-")) isValid = false;

        try {
            Double d = Double.parseDouble(String.valueOf(price));
        }catch (Exception e) {
            isValid = false;
        }

        return isValid;
    }
}
