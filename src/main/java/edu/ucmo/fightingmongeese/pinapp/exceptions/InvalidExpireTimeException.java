package edu.ucmo.fightingmongeese.pinapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

/**
 * Custom exception that causes Spring to return
 * a 400 response if a new PIN is already
 * expired
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidExpireTimeException extends RuntimeException {

    public InvalidExpireTimeException(LocalDateTime expireDateTime) {
        super("Supplied new PIN was already expired.  Expiration given: " + expireDateTime);
    }
}
