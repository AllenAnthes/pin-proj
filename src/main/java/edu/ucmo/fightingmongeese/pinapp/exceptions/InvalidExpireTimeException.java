package edu.ucmo.fightingmongeese.pinapp.exceptions;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * Custom exception that causes Spring to return
 * a 400 response if a new PIN is already
 * expired
 */
public class InvalidExpireTimeException extends BaseCustomRequestException {

    public InvalidExpireTimeException(LocalDateTime expireDateTime) {
        super("Supplied new PIN was already expired.  Expiration given: " + expireDateTime, HttpStatus.BAD_REQUEST);
    }
}
