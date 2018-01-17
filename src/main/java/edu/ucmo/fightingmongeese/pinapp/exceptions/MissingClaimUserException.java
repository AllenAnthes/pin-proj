package edu.ucmo.fightingmongeese.pinapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

/**
 * Custom exception that causes Spring to return a
 * 400 response if a claim is sent without a user
 * identified in the request body
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MissingClaimUserException extends RuntimeException {

    public MissingClaimUserException(Map<String, String> payload) {
        super("PIN Claim request missing `user` in request body.  Payload: " + payload);
    }
}
