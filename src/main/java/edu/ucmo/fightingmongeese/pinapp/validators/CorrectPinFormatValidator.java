package edu.ucmo.fightingmongeese.pinapp.validators;

import edu.ucmo.fightingmongeese.pinapp.models.Pin;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CorrectPinFormatValidator implements ConstraintValidator<CorrectPinFormat, String> {
    public void initialize(CorrectPinFormat constraint) {
    }

    public boolean isValid(String pinString, ConstraintValidatorContext context) {
        return pinString != null && Pin.correctPinFormat(pinString);
    }
}
