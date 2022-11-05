package com.jchaaban.cmsshoppingcard.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CategoryValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)

public @interface CategoryValidatorConstraint {
    String message() default "Invalid Category";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
