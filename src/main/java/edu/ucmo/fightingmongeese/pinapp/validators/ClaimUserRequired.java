package edu.ucmo.fightingmongeese.pinapp.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ClaimUserRequiredValidator.class)
public @interface ClaimUserRequired {

    String message() default "User required for claim";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

