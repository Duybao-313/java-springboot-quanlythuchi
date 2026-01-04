package com.duybao.QUANLYCHITIEU.Validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)

@Constraint(
        validatedBy = {UserNameValidator.class}
)
public @interface UniqueUserName {
    String message() default "USERNAME_ALREADY_EXISTS";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
