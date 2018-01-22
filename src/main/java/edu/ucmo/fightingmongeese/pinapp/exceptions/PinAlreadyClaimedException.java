package edu.ucmo.fightingmongeese.pinapp.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Custom exception that causes Spring to return
 * a 400 response when used PIN is claimed
 */
public class PinAlreadyClaimedException extends BaseCustomRequestException {

    public PinAlreadyClaimedException(String PIN) {
        super("PIN was found but had already been claimed.  PIN: " + PIN, HttpStatus.BAD_REQUEST);
    }

}
