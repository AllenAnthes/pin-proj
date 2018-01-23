package edu.ucmo.fightingmongeese.pinapp.validators;

import edu.ucmo.fightingmongeese.pinapp.components.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class ExpireTimeValidator implements ConstraintValidator<ExpireTime, LocalDateTime> {

    @Autowired
    private DateTime dateTime;

    public ExpireTimeValidator() {}


    public void initialize(ExpireTime constraint) {
    }

    public boolean isValid(LocalDateTime obj, ConstraintValidatorContext context) {
        return (obj == null || obj.isAfter(dateTime.now()));
    }
}
