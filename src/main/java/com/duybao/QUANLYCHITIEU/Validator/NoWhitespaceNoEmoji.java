package com.duybao.QUANLYCHITIEU.Validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NoWhitespaceNoEmojiValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface NoWhitespaceNoEmoji {
    String message() default "PASSWORD_INVALID_CHARS";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}