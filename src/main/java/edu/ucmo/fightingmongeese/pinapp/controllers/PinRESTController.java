package edu.ucmo.fightingmongeese.pinapp.controllers;

import edu.ucmo.fightingmongeese.pinapp.exceptions.*;
import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import edu.ucmo.fightingmongeese.pinapp.repository.PinRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@EnableResourceServer
public class PinRESTController {

    @Autowired
    private PinRepository pinRepository;

    private static final Logger logger = LoggerFactory.getLogger(PinRESTController.class);

    /**
     * Method for handling cancel requests.  Works by simply having the user claim their own pin
     *
     * @param pin     The supplied PIN must have the claim_user, pin, and claim_account fields supplied
     * @param request
     */
    @PostMapping(value = "/cancel")
    public Pin cancel(@Validated(Pin.Cancel.class) @RequestBody Pin pin, HttpServletRequest request) {

        logger.info("Cancel request received from user: {} at {} with PIN: {} for account: {}",
                pin.getClaim_user(), request.getRemoteAddr(), pin.getPin(), pin.getAccount());

        pin = validateCancel(pin.getPin(), pin.getClaim_user());

        pin.setClaimIp(request.getRemoteAddr());
        pin.setClaim_timestamp(LocalDateTime.now());

        pin = pinRepository.save(pin);
        logger.info("Pin successfully canceled: Account: {} | PIN: {} | IP: {}", pin.getAccount(), pin.getPin(), request.getRemoteAddr());
        return pin;
    }

    /**
     * Method for handling PIN claim attempts.  All data is passed in the request body via JSON
     * <p>
     * Besides basic param validation there is currently no authentication implemented.
     *
     * @param pin
     * @param request Request metadata from Spring
     */
    @PostMapping(value = "/claim")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Pin claim(@Validated(Pin.Claim.class) @RequestBody Pin pin, HttpServletRequest request) {

        logger.info("Claim received from user: {} at {} with PIN: {}",
                pin.getClaim_user(), request.getRemoteAddr(), pin.getPin());

        pin = validateClaim(pin.getPin(), pin.getClaim_user());

        pin.setClaimIp(request.getRemoteAddr());
        pin.setClaim_timestamp(LocalDateTime.now());

        pin = pinRepository.save(pin);
        logger.info("Pin successfully claimed: Account: {} | PIN: {} | IP: {}", pin.getAccount(), pin.getPin(), request.getRemoteAddr());
        return pin;
    }

    /**
     * Method for handling adding new one-time-use PINs to the database.
     *
     * @param pin     The PIN will be supplied to the method via the Request Body in JSON.
     *                e.g.:
     *                {
     *                "account": "bob",
     *                "create_user": "user"
     *                }
     *                <p>
     *                Spring/JPA handles the translation between JSON and a Java object.
     * @param request Request metadata from Spring
     */
    @PostMapping(value = "/new")
    public Pin add(@Validated(Pin.Add.class) @RequestBody Pin pin, HttpServletRequest request) {

        // TODO: Better input validation

        validateNewPin(pin);

        SecureRandom random = new SecureRandom();
        // TODO: !! DON'T LET THIS GO TO PROD WITH THE SEED !!
        random.setSeed("totessecure".getBytes());

        boolean notUnique;
        String pinString;

        do {
            int randomPin = random.nextInt(1000000);
            pinString = String.format("%06d", randomPin);
            notUnique = pinRepository.findByPin(pinString).isPresent();
        } while (notUnique);

        pin.setPin(pinString);
        pin.setCreate_timestamp(LocalDateTime.now());

        logger.info("New PIN received from User: {} at {} -- Account: {} | PIN: {}",
                pin.getCreate_user(), request.getRemoteAddr(), pin.getAccount(), pin.getPin());
        pin.setCreate_ip(request.getRemoteAddr());

        if (pin.getExpire_timestamp() == null) {
            // Default expire time set to 30 minutes
            pin.setExpire_timestamp(LocalDateTime.now().plusMinutes(30));
        }
        pin = this.pinRepository.save(pin);
        logger.info("New PIN successfully saved: Account: {} | PIN: {} | IP: {}", pin.getAccount(), pin.getPin(), request.getRemoteAddr());
        return pin;
    }

    private Pin validateCancel(String pin, String user) {


        Pin res = pinRepository.findByPin(pin).orElseThrow(
                () -> new PinNotFoundException(pin));
        res.setClaim_user(user);
        return res;
    }

    private void validateNewPin(Pin newPin) {
        if (newPin.getExpire_timestamp() != null && newPin.getExpire_timestamp().isBefore(LocalDateTime.now())) {
            throw new InvalidExpireTimeException(newPin.getExpire_timestamp());
        }
        Optional<Pin> pin = pinRepository.findActivePin(newPin.getAccount());
        if (pin.isPresent()) {
            throw new AccountHasActivePinException(pin.get().getAccount(), pin.get().getExpire_timestamp());
        }
    }

    /**
     * Utility method for validating the correct params are sent to the claim handler.  If request is found to
     * be invalid method returns an exception to be logged and Spring sends the requester an appropriate
     * HTTP response code and message.
     * <p>
     * If request params are all valid the method returns the current PIN from the persistence repository
     *
     * @param requestPin
     * @param claim_user
     */
    private Pin validateClaim(String requestPin, String claim_user) {
        Pin pin = this.pinRepository.findByPin(requestPin).orElseThrow(
                () -> new PinNotFoundException(requestPin)
        );
        pin.setClaim_user(claim_user);

        if (LocalDateTime.now().isAfter(pin.getExpire_timestamp())) {
            throw new ExpiredPinException(requestPin);
        }

        if (pin.getClaim_timestamp() != null) {
            throw new PinAlreadyClaimedException(requestPin);
        }

        if (claim_user == null) {
            throw new MissingClaimUserException(pin);
        }

        return pin;
    }
}

