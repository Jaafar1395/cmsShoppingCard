package com.jchaaban.cmsshoppingcard.validators;

import com.jchaaban.cmsshoppingcard.models.data.Category;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CategoryValidator implements ConstraintValidator<CategoryValidatorConstraint, Category> {

    @Override
    public void initialize(CategoryValidatorConstraint category) {}

    @Override
    public boolean isValid(Category category, ConstraintValidatorContext constraintValidatorContext) {

        constraintValidatorContext.disableDefaultConstraintViolation();

        if (category == null) {
            customMessageForValidation(constraintValidatorContext, "Please set a category");
            return false;
        }

        if (category.getName() == null || category.getName() == "") {
            customMessageForValidation(constraintValidatorContext, "Please se a valid category");
            return false;
        }
        return true;
    }

    private void customMessageForValidation(ConstraintValidatorContext constraintContext, String message) {
        constraintContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }

}
