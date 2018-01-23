package edu.ucmo.fightingmongeese.pinapp.validators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AccountRequiredValidator implements ConstraintValidator<AccountRequired, String> {

    private static final Logger logger = LoggerFactory.getLogger(AccountRequiredValidator.class);

    public void initialize(AccountRequired constraint) {
    }

    public boolean isValid(String account, ConstraintValidatorContext context) {
        if (account == null) {
            logger.warn("Received new PIN without account");
            return false;
        }
        if (!account.matches("\\p{Alnum}+")) {
            logger.warn("Received account that was not alphanumeric.  Account: {}", account);
            return false;
        }
        return true;
    }

}
