package com.example.fooddiary.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = MaxTotalGramsValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxTotalGrams {
    String message() default "Сумма белков, жиров и углеводов не может превышать 100 г";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
