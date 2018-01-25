package edu.ucmo.fightingmongeese.pinapp.validators;

import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import edu.ucmo.fightingmongeese.pinapp.repository.PinRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class SingleActivePinValidator implements ConstraintValidator<SingleActivePin, String> {


    @Autowired
    public PinRepository pinRepository;


    public void initialize(SingleActivePin constraint) {
    }

    public boolean isValid(String account, ConstraintValidatorContext context) {
        Optional<Pin> optionalPin = pinRepository.findActivePin(account);
        return !optionalPin.isPresent();
    }
}
