package edu.ucmo.fightingmongeese.pinapp.validators;

import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import edu.ucmo.fightingmongeese.pinapp.repository.PinRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class UnclaimedValidator implements ConstraintValidator<Unclaimed, String> {

    private PinRepository pinRepository;

    private static final Logger logger = LoggerFactory.getLogger(UnclaimedValidator.class);


    @Autowired
    public UnclaimedValidator(PinRepository pinRepository) {
        this.pinRepository = pinRepository;
    }

    public void initialize(Unclaimed constraint) {
    }

    public boolean isValid(String claimPin, ConstraintValidatorContext context) {

        Pin pin = pinRepository.findByPin(claimPin).orElse(null);
        if (pin != null && pin.getClaim_user() != null) {
            logger.warn("Received PIN with missing claim_user.");
            return false;
        }
        return true;
    }
}
