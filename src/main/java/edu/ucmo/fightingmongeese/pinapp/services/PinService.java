package edu.ucmo.fightingmongeese.pinapp.services;


import edu.ucmo.fightingmongeese.pinapp.components.DateTime;
import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import edu.ucmo.fightingmongeese.pinapp.repository.PinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

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
     * @param pin
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
     * Method handles passing claim to validator and persisting the claim in the database
     * <p>
     * Besides basic param validation there is currently no authentication implemented.
     *
     * @param claimPin
     */
    public Pin claim(Pin claimPin) {
        Pin pin = pinRepository.findByPin(claimPin.getPin()).orElse(new Pin());
        pin.setClaim_user(claimPin.getClaim_user());
        pin.setClaim_ip(claimPin.getClaim_ip());
        pin.setClaim_timestamp(dateTime.now());
        return pinRepository.save(pin);
    }

    /**
     * Business logic for canceling an active PIN.
     * The service assumes a valid user is performing the request
     * and only validates the PIN is currently active in the database
     *
     * @param cancelPin
     */
    public Pin cancel(Pin cancelPin) {

        Pin pin = pinRepository.findByPin(cancelPin.getPin()).orElse(new Pin());
        pin.setClaim_user(cancelPin.getClaim_user());
        cancelPin.setClaim_timestamp(dateTime.now());
        return pinRepository.save(cancelPin);
    }
}
