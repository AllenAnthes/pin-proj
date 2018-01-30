package edu.ucmo.fightingmongeese.pinapp.services;


import edu.ucmo.fightingmongeese.pinapp.components.DateTime;
import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import edu.ucmo.fightingmongeese.pinapp.repository.PinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

/**
 * Primary class containing the business logic behind the REST API
 * <p>
 * As validation is completed before the request arrives at this point
 * the method simply updates the PIN in the repository with the necessary
 * information.
 * <p>
 * Currently there is no authentication implemented and the service assumes any request
 * received is valid.
 * <p>
 * Old record scavenging is not currently implemented but could easily be added.
 */
@Service
public class PinService {

    private PinRepository pinRepository;

    private DateTime dateTime;

    @Autowired
    public PinService(PinRepository pinRepository, DateTime dateTime) {
        this.pinRepository = pinRepository;
        this.dateTime = dateTime;
    }

    /**
     * Method containing the business logic for adding a new PIN to the database.
     * A 6 digit PIN is created using Java's SecureRandom class
     *
     * @param pin PIN received from the client to be added to the database
     */
    public Pin add(Pin pin) {

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
     * Method for handling claims sent to the REST API.
     *
     * @param claimPin PIN sent from the client to be claimed.
     */
    public Pin claim(Pin claimPin) {
        Pin pin = pinRepository.findByPin(claimPin.getPin()).orElse(new Pin());
        pin.setClaim_user(claimPin.getClaim_user());
        pin.setClaim_ip(claimPin.getClaim_ip());
        pin.setClaim_timestamp(dateTime.now());
        return pinRepository.save(pin);
    }

    /**
     * Method used to interact with the database to cancel an active PIN
     *
     * @param cancelPin PIN sent from the client to be canceled.
     */
    public Pin cancel(Pin cancelPin) {

        Pin pin = pinRepository.findByPin(cancelPin.getPin()).orElse(new Pin());
        pin.setClaim_ip(cancelPin.getClaim_ip());
        pin.setClaim_user(cancelPin.getClaim_user());
        pin.setClaim_timestamp(dateTime.now());
        return pinRepository.save(pin);
    }
}
