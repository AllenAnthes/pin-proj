package edu.ucmo.fightingmongeese.pinapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception that causes Spring to return
 * a 400 response when expired PIN is claimed
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExpiredPinException extends RuntimeException {

    public ExpiredPinException(String PIN) {
        super("PIN was found but has expired.  PIN: " + PIN);
    }
}
