package edu.ucmo.fightingmongeese.pinapp.controllers;


import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import edu.ucmo.fightingmongeese.pinapp.repository.PinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TestingController {

    @Autowired
    PinRepository pinRepository;


    @PostMapping("/all")
    public List<Pin> all() {
        return pinRepository.findAll();
    }


    /**
     * Temporary method for resetting a PIN back to
     * unclaimed status for testing purposes
     *
     * @param pin
     */
    @PostMapping("/unclaim")
    public Pin unClaim(@RequestBody Pin pin) {
        pin = pinRepository.findOne(pin.getOid());
        pin.setClaim_user(null);
        pin.setClaim_timestamp(null);
        pin.setClaimIp(null);
        return pinRepository.save(pin);
    }

}
