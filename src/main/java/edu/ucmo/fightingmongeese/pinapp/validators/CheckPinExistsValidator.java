package edu.ucmo.fightingmongeese.pinapp.validators;

import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import edu.ucmo.fightingmongeese.pinapp.repository.PinRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class CheckPinExistsValidator implements ConstraintValidator<CheckPinExists, String> {

    private final PinRepository pinRepository;

    @Autowired
    public CheckPinExistsValidator(PinRepository pinRepository) {
        this.pinRepository = pinRepository;
    }

    public void initialize(CheckPinExists constraint) {
    }

    public boolean isValid(String pin, ConstraintValidatorContext context) {
        // We short circuit here so we  don't query the repo if pin format is invalid
        if (pin == null || !Pin.correctPinFormat(pin) || !Pin.isValidLuhn(pin)) {
            return true;
        }

        return pinRepository.findByPin(pin).isPresent();
    }

}
