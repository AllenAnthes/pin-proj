package edu.ucmo.fightingmongeese.pinapp.controllers;


import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import edu.ucmo.fightingmongeese.pinapp.repository.PinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Temporary Controller for the Thymeleaf based front-end
 * Will most likely be removed before prod
 */
@Controller
public class PinController {

    @Autowired
    PinRepository pinRepository;

    private Pin pin;


    @RequestMapping(value = "pins/new", method = RequestMethod.POST)
    public String addPIN(@ModelAttribute("pin") Pin pin, Model model) {
        pinRepository.save(pin);
        List<Pin> pins = pinRepository.findAll();
        model.addAttribute("pins", pins);
        return "pin-table";
    }


    @RequestMapping(value = "pins/list", method = RequestMethod.POST)
    public String editPIN(@ModelAttribute("pin") Pin pin, Model model) {
        Pin oldpin = pinRepository.findOne(pin.getOid());
        pinRepository.save(pin);
        List<Pin> pins = pinRepository.findAll();
        this.pin = pin;
        model.addAttribute("pin", this.pin);
        model.addAttribute("pins", pins);

        return "pin-table";
    }

    @RequestMapping(value = "pins/list", method = RequestMethod.GET)
    public String showCredentials(Model model) {
        List<Pin> pins = pinRepository.findAll();
        model.addAttribute("pins", pins);
        if (pin == null) {
            model.addAttribute("pin", new Pin());
        } else {
            model.addAttribute("pin", this.pin);
        }
        return "pin-table";
    }
}
