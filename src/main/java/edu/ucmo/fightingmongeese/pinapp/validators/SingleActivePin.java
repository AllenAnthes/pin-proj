package edu.ucmo.fightingmongeese.pinapp.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;


@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SingleActivePinValidator.class)
public @interface SingleActivePin {
    String message() default "An account can only have one PIN active at a time.";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

