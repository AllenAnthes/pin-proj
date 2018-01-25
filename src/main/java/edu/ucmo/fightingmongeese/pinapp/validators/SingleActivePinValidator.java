package edu.ucmo.fightingmongeese.pinapp.validators;

import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import edu.ucmo.fightingmongeese.pinapp.repository.PinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.Serializable;
import java.util.Optional;

public class SingleActivePinValidator implements ConstraintValidator<SingleActivePin, String>, Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private PinRepository pinRepository;

    public void setPinRepository(PinRepository pinRepository) {
        this.pinRepository = pinRepository;
    }

    public void initialize(SingleActivePin constraint) {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    public boolean isValid(String account, ConstraintValidatorContext context) {
        // return true as the account being active isn't the actual violation
        if (account == null) {
            return true;
        }
        Optional<Pin> optionalPin = pinRepository.findActivePin(account);
        return !optionalPin.isPresent();
    }
}
