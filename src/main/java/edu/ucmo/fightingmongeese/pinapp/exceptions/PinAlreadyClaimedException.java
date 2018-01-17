package edu.ucmo.fightingmongeese.pinapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception that causes Spring to return
 * a 400 response when used PIN is claimed
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PinAlreadyClaimedException extends RuntimeException {

    public PinAlreadyClaimedException(String PIN) {
        super("PIN was found but had already been claimed.  PIN: " + PIN);
    }
}
