package edu.ucmo.fightingmongeese.pinapp.validators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CreateUserRequiredValidator implements ConstraintValidator<CreateUserRequired, String> {

    private static final Logger logger = LoggerFactory.getLogger(CreateUserRequiredValidator.class);

    public void initialize(CreateUserRequired constraint) {
    }

    public boolean isValid(String user, ConstraintValidatorContext context) {
        if (user == null) {
            logger.warn("Received new PIN without create_user");
            return false;
        }
        return true;
    }

}
