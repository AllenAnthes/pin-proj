package edu.ucmo.fightingmongeese.pinapp.validators;

import edu.ucmo.fightingmongeese.pinapp.models.Pin;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class Mod10ValidValidator implements ConstraintValidator<Mod10Valid, String> {
    public void initialize(Mod10Valid constraint) {
    }

    public boolean isValid(String pin, ConstraintValidatorContext context) {
        return pin == null ||  !Pin.correctPinFormat(pin) || Pin.isValidLuhn(pin);
    }
}
