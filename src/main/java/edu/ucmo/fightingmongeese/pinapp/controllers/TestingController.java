package edu.ucmo.fightingmongeese.pinapp.controllers;


import edu.ucmo.fightingmongeese.pinapp.components.DateTime;
import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import edu.ucmo.fightingmongeese.pinapp.repository.PinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for routing API calls to be used during development.
 */
@RestController
@RequestMapping("/test")
public class TestingController {

    private final PinRepository pinRepository;

    private final DateTime dateTime;

    @Autowired
    public TestingController(PinRepository pinRepository, DateTime dateTime) {
        this.pinRepository = pinRepository;
        this.dateTime = dateTime;
    }

    /**
     * Method to simply query the pin database and return all pins
     * currently stored.
     */
    @PostMapping("/all")
    public List<Pin> all() {
        return pinRepository.findAll();
    }


    /**
     * Development testing method for resetting a PIN back to
     * unclaimed status for testing purposes
     *
     * @param pin
     */
    @PostMapping("/unclaim")
    public Pin unClaim(@RequestBody Pin pin) {
        pin = pinRepository.findOne(pin.getOid());
        pin.setClaim_user(null);
        pin.setClaim_timestamp(null);
        pin.setClaim_ip(null);
        return pinRepository.save(pin);
    }

    /**
     * Development testing method for adding
     * 30 minutes to the expiration time of
     * given PIN
     * @param pin
     * @return
     */
    @PostMapping("resetExpiration")
    public Pin resetExpiration(@RequestBody Pin pin) {
        pin = pinRepository.findOne(pin.getOid());
        pin.setExpire_timestamp(dateTime.now().plusMinutes(30));
        return pinRepository.save(pin);
    }
}
