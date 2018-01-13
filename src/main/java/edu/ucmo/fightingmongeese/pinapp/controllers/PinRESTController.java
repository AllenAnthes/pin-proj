package edu.ucmo.fightingmongeese.pinapp.controllers;

import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import edu.ucmo.fightingmongeese.pinapp.repository.PinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class PinRESTController {

    @Autowired
    private PinRepository pinRepository;

    private static final Logger logger = Logger.getLogger(PinRESTController.class.getName());


    @RequestMapping(value = "/{account}/{pin}", method = RequestMethod.POST)
    public Pin claim(@PathVariable String account, @PathVariable String pin, @RequestBody Map<String, String> payload, HttpServletRequest request) {

        logger.info(String.format("Claim received: Account: %s | PIN: %s | IP: %s", account, pin, request.getRemoteAddr()));
        Pin oldPin = this.pinRepository.findByAccountAndPin(account, pin).orElseThrow(
                () -> new AccountNotFoundException(account)
        );

        if (oldPin.getExpire_timestamp().before(Timestamp.valueOf(LocalDateTime.now()))) {
            throw new ExpiredPinException(pin);
        }

        if (oldPin.getClaim_timestamp() != null) {
            throw new PinAlreadyClaimedException(pin);
        }

        oldPin.setClaim_ip(request.getRemoteAddr());
        oldPin.setClaim_user(payload.get("user"));
        oldPin.setClaim_timestamp(Timestamp.valueOf(LocalDateTime.now()));

        Pin res = pinRepository.save(oldPin);
        logger.info(String.format("Pin successfully claimed: Account: %s | PIN: %s | IP: %s", account, pin, request.getRemoteAddr()));
        return res;
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public Pin add(@RequestBody Pin pin, HttpServletRequest request) {

        logger.info(String.format("New PIN received: Account: %s | PIN: %s | IP: %s", pin.getAccount(), pin.getPin(), request.getRemoteAddr()));
        pin.setCreate_ip(request.getRemoteAddr());

        if (pin.getExpire_timestamp() == null) {
            // Default expire time set to 48 hours
            pin.setExpire_timestamp(Timestamp.valueOf(LocalDateTime.now().plusDays(2)));
        }
        pin = this.pinRepository.save(pin);
        logger.info(String.format("New PIN successfully saved: Account: %s | PIN: %s | IP: %s", pin.getAccount(), pin.getPin(), request.getRemoteAddr()));
        return pin;
    }
}

/**
 * Custom exception that causes Spring to return
 * a 404 response when account is not found
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String account) {
        super("could not find user '" + account + "'.");
    }
}

/**
 * Custom exception that causes Spring to return
 * a 400 response when expired PIN is claimed
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
class ExpiredPinException extends RuntimeException {

    public ExpiredPinException(String PIN) {
        super("PIN was found but has expired.  PIN: " + PIN);
    }
}

/**
 * Custom exception that causes Spring to return
 * a 400 response when used PIN is claimed
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
class PinAlreadyClaimedException extends RuntimeException {

    public PinAlreadyClaimedException(String PIN) {
        super("PIN was found but had already been claimed.  PIN: " + PIN);
    }
}
