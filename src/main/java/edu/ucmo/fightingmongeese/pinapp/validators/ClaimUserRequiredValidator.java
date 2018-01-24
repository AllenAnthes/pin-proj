package edu.ucmo.fightingmongeese.pinapp.validators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ClaimUserRequiredValidator implements ConstraintValidator<ClaimUserRequired, String> {

    private static final Logger logger = LoggerFactory.getLogger(ClaimUserRequiredValidator.class);


    public void initialize(ClaimUserRequired constraint) {
   }

   public boolean isValid(String user, ConstraintValidatorContext context) {
      if (user == null) {
          logger.warn("Received claim without a claim_user paramter");
          return false;
      }
      return true;
   }
}
