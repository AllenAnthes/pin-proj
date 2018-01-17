package edu.ucmo.fightingmongeese.pinapp.controllers;

import edu.ucmo.fightingmongeese.pinapp.exceptions.*;
import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import edu.ucmo.fightingmongeese.pinapp.repository.PinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class PinRESTController {

    @Autowired
    private PinRepository pinRepository;

    private static final Logger logger = Logger.getLogger(PinRESTController.class.getName());


    /**
     * Method for handling PIN claim attempts.  Currently the account and requested PIN are passed in the URL
     * and the user is sent as JSON in the body.
     *
     * Besides basic param validation there is currently no authentication implemented.
     *
     * @param account       The account associated with the one-time-use PIN
     * @param requestPin    The PIN being claimed
     * @param payload       The JSON payload that must contain the user who is doing the claim
     * @param request       Request metadata from Spring
     */
    @RequestMapping(value = "/{account}/{requestPin}", method = RequestMethod.POST)
    public Pin claim(@PathVariable String account, @PathVariable String requestPin, @RequestBody Map<String, String> payload, HttpServletRequest request) {

        logger.info(String.format("Claim received from user: %s at %s -- Account: %s | PIN: %s | IP: ",
                payload.getOrDefault("user", "NO USER"), request.getRemoteAddr(), account, requestPin));

        Pin pin = validateClaim(account, requestPin, payload);

        pin.setClaim_ip(request.getRemoteAddr());
        pin.setClaim_user(payload.get("user"));
        pin.setClaim_timestamp(LocalDateTime.now());

        Pin res = pinRepository.save(pin);
        logger.info(String.format("Pin successfully claimed: Account: %s | PIN: %s | IP: %s", account, requestPin, request.getRemoteAddr()));
        return res;
    }

    /**
     * Method for handling adding new one-time-use PINs to the database.
     *
     * @param pin
     *      The PIN will be supplied to the method via the Request Body in JSON.
     *      e.g.:
     *              {
     *              "account": "bob",
     *              "create_user": "user"
     *              }
     *
     *      Spring/JPA handles the translation between JSON and a Java object.
     *
     * @param request   Request metadata from Spring
     */
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public Pin add(@RequestBody Pin pin, HttpServletRequest request) {

        if (pin.getExpire_timestamp() != null && pin.getExpire_timestamp().isBefore(LocalDateTime.now())) {
            throw new InvalidExpireTimeException(pin.getExpire_timestamp());
        }

        SecureRandom random = new SecureRandom();
        boolean notUnique;
        String pinString;

        do {
            int randomPin = random.nextInt(1000000);
            pinString = String.format("%05d", randomPin);
            notUnique = pinRepository.findByPin(pinString).isPresent();
        } while (notUnique);

        pin.setPin(pinString);
        pin.setCreate_timestamp(LocalDateTime.now());

        logger.info(String.format("New PIN received from User: %s at %s -- Account: %s | PIN: %s",
                pin.getCreate_user(), request.getRemoteAddr(), pin.getAccount(), pin.getPin()));
        pin.setCreate_ip(request.getRemoteAddr());

        if (pin.getExpire_timestamp() == null) {
            // Default expire time set to 48 hours
            pin.setExpire_timestamp(LocalDateTime.now().plusDays(2));
        }
        pin = this.pinRepository.save(pin);
        logger.info(String.format("New PIN successfully saved: Account: %s | PIN: %s | IP: %s", pin.getAccount(), pin.getPin(), request.getRemoteAddr()));
        return pin;
    }

    /**
     * Utility method for validating the correct params are sent to the claim handler.  If request is found to
     * be invalid method returns an exception to be logged and Spring sends the requester an appropriate
     * HTTP response code and message.
     *
     * If request params are all valid the method returns the current PIN from the persistence repository
     *
     * @param account
     * @param requestPin
     * @param payload
     */
    private Pin validateClaim(String account, String requestPin, Map<String, String> payload) {
        Pin pin = this.pinRepository.findByAccountAndPin(account, requestPin).orElseThrow(
                () -> new AccountNotFoundException(account)
        );

        if (LocalDateTime.now().isAfter(pin.getExpire_timestamp())) {
            throw new ExpiredPinException(requestPin);
        }

        if (pin.getClaim_timestamp() != null) {
            throw new PinAlreadyClaimedException(requestPin);
        }

        if (!payload.containsKey("user")) {
            throw new MissingClaimUserException(payload);
        }

        return pin;
    }
}

