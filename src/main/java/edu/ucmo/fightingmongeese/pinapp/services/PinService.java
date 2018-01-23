package edu.ucmo.fightingmongeese.pinapp.services;


import edu.ucmo.fightingmongeese.pinapp.components.DateTime;
import edu.ucmo.fightingmongeese.pinapp.exceptions.*;
import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import edu.ucmo.fightingmongeese.pinapp.repository.PinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@Service
public class PinService {

    @Autowired
    PinRepository pinRepository;

    @Autowired
    private DateTime dateTime;


    /**
     * Method containing the business logic for adding a new PIN to the database.
     * A 6 digit PIN is created using Java's SecureRandom class
     *
     * @param pin
     * @param remoteAddr IP Address of the user claiming PIN
     */
    public Pin add(Pin pin, String remoteAddr) {


        // TODO: Better input validation
//        validateNewPin(pin);

        pin.setCreate_ip(remoteAddr);
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
        pin.setCreate_timestamp(dateTime.now());

        if (pin.getExpire_timestamp() == null) {
            // Default expire time set to 30 minutes
            pin.setExpire_timestamp(dateTime.now().plusMinutes(30));
        }
        pin = this.pinRepository.save(pin);
        return pin;
    }

    /**
     * Method handles passing claim to validator and persisting the claim in the database
     * <p>
     * Besides basic param validation there is currently no authentication implemented.
     *
     * @param pin
     * @param remoteAddr IP Address of the user claiming PIN
     */
    public Pin claim(Pin pin, String remoteAddr) {
        pin = validateClaim(pin.getPin(), pin.getClaim_user());
        pin.setClaimIp(remoteAddr);
        pin.setClaim_timestamp(dateTime.now());
        return pinRepository.save(pin);
    }

    /**
     * Business logic for canceling an active PIN.
     * The service assumes a valid user is performing the request
     * and only validates the PIN is currently active in the database
     *
     * @param pin
     * @param remoteAddr IP Address of the user claiming PIN
     */
    public Pin cancel(Pin pin, String remoteAddr) {
        pin = validateCancel(pin.getPin(), pin.getClaim_user());

        pin.setClaimIp(remoteAddr);
        pin.setClaim_timestamp(dateTime.now());

        return pinRepository.save(pin);
    }


    /**
     * The following are utility methods for validating the correct params are sent to the handler.  If request is found to
     * be invalid method returns an exception to be logged and Spring sends the requester an appropriate
     * HTTP response code and message.
     * <p>
     * If request params are all valid the method returns the current PIN from the persistence repository
     *
     * @param requestPin
     * @param claim_user
     */
    private Pin validateClaim(String requestPin, String claim_user) {
        Pin pin = pinRepository.findByPin(requestPin).orElseThrow(
                () -> new PinNotFoundException(requestPin)
        );
        pin.setClaim_user(claim_user);

        if (dateTime.now().isAfter(pin.getExpire_timestamp())) {
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

    private Pin validateCancel(String pin, String user) {
        Pin res = pinRepository.findByPin(pin).orElseThrow(
                () -> new PinNotFoundException(pin));
        res.setClaim_user(user);
        return res;
    }

    public void validateNewPin(Pin newPin) {
        if (newPin.getExpire_timestamp() != null && newPin.getExpire_timestamp().isBefore(dateTime.now())) {
            throw new InvalidExpireTimeException(newPin.getExpire_timestamp());
        }
        Optional<Pin> pin = pinRepository.findActivePin(newPin.getAccount());
        if (pin.isPresent()) {
            throw new AccountHasActivePinException(pin.get().getAccount(), pin.get().getExpire_timestamp());
        }
    }
}
