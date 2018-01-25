package edu.ucmo.fightingmongeese.pinapp.validators;

import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import edu.ucmo.fightingmongeese.pinapp.repository.PinRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UnclaimedValidator implements ConstraintValidator<Unclaimed, String> {

    private PinRepository pinRepository;


    @Autowired
    public UnclaimedValidator(PinRepository pinRepository) {
        this.pinRepository = pinRepository;
    }

    public void initialize(Unclaimed constraint) {
    }

    public boolean isValid(String claimPin, ConstraintValidatorContext context) {

        Pin pin = pinRepository.findByPin(claimPin).orElse(null);
        return pin == null || pin.getClaim_user() == null;
    }
}
