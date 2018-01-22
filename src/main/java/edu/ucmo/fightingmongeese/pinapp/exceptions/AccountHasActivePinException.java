package edu.ucmo.fightingmongeese.pinapp.exceptions;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * Custom exception that causes Spring to return
 * a 400 response when user attempts to create a new PIN
 * when one is already active
 */
public class AccountHasActivePinException extends BaseCustomRequestException {

    public AccountHasActivePinException(String PIN, LocalDateTime expire_timestamp) {
        super("Account has a currently active PIN, only one allowed at a time." +
                String.format(" Active PIN: %s | Active PIN expire time: %s", PIN, expire_timestamp), HttpStatus.BAD_REQUEST);
    }
}
