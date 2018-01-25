package edu.ucmo.fightingmongeese.pinapp.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AccountFormatValidator implements ConstraintValidator<AccountFormat, String> {


    public void initialize(AccountFormat constraint) {
    }

    public boolean isValid(String account, ConstraintValidatorContext context) {
        return account == null || account.matches("\\p{Alnum}+");
    }

}
