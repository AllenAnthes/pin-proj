package edu.ucmo.fightingmongeese.pinapp.exceptions;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;

/**
 * Custom exception that causes Spring to return
 * a 404 response when account is not found
 */
public class PinNotFoundException extends BaseCustomRequestException {
    public PinNotFoundException(String pin) {
        super("could not find pin in database.  Pin provided: " + pin + ".", HttpStatus.NOT_FOUND);
    }
}
