package edu.ucmo.fightingmongeese.pinapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

/**
 * Custom exception that causes Spring to return
 * a 400 response when user attempts to create a new PIN
 * when one is already active
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AccountHasActivePinException extends RuntimeException {

    public AccountHasActivePinException(String PIN, LocalDateTime expire_timestamp) {
        super("Account has a currently active PIN, only one allowed at a time.\n" +
                String.format("Active PIN: %s | Active PIN expire time: %s", PIN, expire_timestamp));
    }
}
