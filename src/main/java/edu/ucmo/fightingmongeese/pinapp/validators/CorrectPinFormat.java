package edu.ucmo.fightingmongeese.pinapp.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CorrectPinFormatValidator.class)
public @interface CorrectPinFormat {
    String message() default "{edu.ucmo.fightingmongeese.pinapp.validators.CorrectPinFormat.description}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
