package edu.ucmo.fightingmongeese.pinapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception that causes Spring to return
 * a 404 response when account is not found
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String account) {
        super("could not find user '" + account + "'.");
    }
}
