package edu.ucmo.fightingmongeese.pinapp.validators;

import edu.ucmo.fightingmongeese.pinapp.components.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class ExpireTimeValidator implements ConstraintValidator<ExpireTime, LocalDateTime> {

    @Autowired
    private DateTime dateTime;

    private static final Logger logger = LoggerFactory.getLogger(ExpireTimeValidator.class);


    public ExpireTimeValidator() {}


    public void initialize(ExpireTime constraint) {
    }

    public boolean isValid(LocalDateTime obj, ConstraintValidatorContext context) {
        if (obj == null || obj.isAfter(dateTime.now())) {
            return true;
        }
        logger.warn("PIN received with invalid expire time: {}", obj );
        return false;
    }
}
