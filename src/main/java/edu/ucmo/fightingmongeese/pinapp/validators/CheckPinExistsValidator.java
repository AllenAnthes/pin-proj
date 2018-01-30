package edu.ucmo.fightingmongeese.pinapp.validators;

import edu.ucmo.fightingmongeese.pinapp.repository.PinRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckPinExistsValidator implements ConstraintValidator<CheckPinExists, String> {

    private PinRepository pinRepository;

    @Autowired
    public CheckPinExistsValidator(PinRepository pinRepository) {
        this.pinRepository = pinRepository;
    }

    public void initialize(CheckPinExists constraint) {
    }

    public boolean isValid(String pin, ConstraintValidatorContext context) {
        return pinRepository.findByPin(pin).isPresent();
    }

}
