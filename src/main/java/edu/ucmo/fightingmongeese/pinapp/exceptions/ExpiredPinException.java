package edu.ucmo.fightingmongeese.pinapp.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Custom exception that causes Spring to return
 * a 400 response when expired PIN is claimed
 */
public class ExpiredPinException extends BaseCustomRequestException {

    public ExpiredPinException(String PIN) {
        super("PIN was found but has expired.  PIN: " + PIN, HttpStatus.BAD_REQUEST);
    }
}
