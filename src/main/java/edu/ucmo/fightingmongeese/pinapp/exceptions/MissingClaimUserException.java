package edu.ucmo.fightingmongeese.pinapp.exceptions;

import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import org.springframework.http.HttpStatus;

/**
 * Custom exception that causes Spring to return a
 * 400 response if a claim is sent without a user
 * identified in the request body
 */
public class MissingClaimUserException extends BaseCustomRequestException {

    public MissingClaimUserException(Pin pin) {
        super("PIN Claim request missing `user` in request body. | " + pin, HttpStatus.BAD_REQUEST);
    }
}
