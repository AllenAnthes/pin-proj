package edu.ucmo.fightingmongeese.pinapp.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CreateUserRequiredValidator implements ConstraintValidator<CreateUserRequired, String> {


    public void initialize(CreateUserRequired constraint) {
    }

    public boolean isValid(String user, ConstraintValidatorContext context) {
        return user != null;
    }

}
