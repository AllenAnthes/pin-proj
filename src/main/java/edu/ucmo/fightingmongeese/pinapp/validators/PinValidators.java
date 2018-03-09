package edu.ucmo.fightingmongeese.pinapp.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@NotNull(message = "{edu.ucmo.fightingmongeese.defaultPINRequiredMessage}")
@CheckPinExists
@ClaimBeforeExpiration
@CorrectPinFormat
@Unclaimed
@Mod10Valid
@Constraint(validatedBy = {})
public @interface PinValidators {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
