package edu.ucmo.fightingmongeese.pinapp.validators;

import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import org.hibernate.validator.constraints.ConstraintComposition;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.lang.annotation.*;

@NotNull(groups = {Pin.Claim.class, Pin.Cancel.class},
        message = "PIN must be provided in request")
@CheckPinExists(groups = {Pin.Claim.class, Pin.Cancel.class})
@ClaimBeforeExpiration(groups = Pin.Claim.class)
@Unclaimed(groups = Pin.Claim.class)
@Pattern(regexp = "\\p{N}+", message = "PIN must be numeric")
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@ConstraintComposition
//@Documented
public @interface PinValidators {

    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

