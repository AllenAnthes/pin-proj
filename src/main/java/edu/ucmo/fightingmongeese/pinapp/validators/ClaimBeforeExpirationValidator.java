package edu.ucmo.fightingmongeese.pinapp.validators;

import edu.ucmo.fightingmongeese.pinapp.components.DateTime;
import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import edu.ucmo.fightingmongeese.pinapp.repository.PinRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ClaimBeforeExpirationValidator implements ConstraintValidator<ClaimBeforeExpiration, String> {


    private DateTime dateTime;

    private PinRepository pinRepository;

    @Autowired
    public ClaimBeforeExpirationValidator(DateTime dateTime, PinRepository pinRepository) {
        this.dateTime = dateTime;
        this.pinRepository = pinRepository;
    }

    public void initialize(ClaimBeforeExpiration constraint) {
    }

    public boolean isValid(String pin, ConstraintValidatorContext context) {

        Pin pin2 = pinRepository.findByPin(pin).orElse(null);
        return pin2 != null && dateTime.now().isBefore(pin2.getExpire_timestamp());
    }
}