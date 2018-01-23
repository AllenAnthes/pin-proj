package edu.ucmo.fightingmongeese.pinapp.validators;

import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import edu.ucmo.fightingmongeese.pinapp.repository.PinRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class SingleActivePinValidator implements ConstraintValidator<SingleActivePin, String> {


    @Autowired
    public PinRepository pinRepository;

    private static final Logger logger = LoggerFactory.getLogger(SingleActivePinValidator.class);


    public void initialize(SingleActivePin constraint) {
    }

    public boolean isValid(String account, ConstraintValidatorContext context) {
        Optional<Pin> optionalPin = pinRepository.findActivePin(account);
        if (optionalPin.isPresent()) {
            Pin activePin = optionalPin.get();
            logger.warn("Received PIN from user with active PIN.  Account: {} Active PIN: {} Active PIN Expiration: {}",
                    activePin.getAccount(), activePin.getAccount(), activePin.getExpire_timestamp());
            return false;
        }


        return true;
    }
}
