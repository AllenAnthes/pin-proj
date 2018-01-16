package edu.ucmo.fightingmongeese.pinapp.controllers;


import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import edu.ucmo.fightingmongeese.pinapp.models.PinDTO;
import edu.ucmo.fightingmongeese.pinapp.repository.PinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Temporary Controller for the Thymeleaf based front-end
 * Will most likely be removed before prod
 */
@Controller
public class PinController {

    @Autowired
    PinRepository pinRepository;

    private static final Logger logger = Logger.getLogger(PinRESTController.class.getName());

    /**
     * Method for routing new submissions from the Thymeleaf UI to the REST endpoint
     *
     * @param pin     DTO that holds the info to be transferred to the actual endpoint.
     *                Had to use a hack to get the Date and Time to properly translate
     * @param request
     */
    @RequestMapping(value = "pins/new", method = RequestMethod.POST)
    public String add(@ModelAttribute PinDTO pin, HttpServletRequest request) {

        String baseUrl = String.format("%s://%s:%d/api/", request.getScheme(),
                request.getServerName(), request.getServerPort());
        String url = baseUrl + "/new";

        Map<String, String> payload = new HashMap<>();
        payload.put("account", pin.getAccount());
        payload.put("create_user", request.getUserPrincipal().getName());


        if (pin.getExpire_timestamp() != null && pin.getExpire_time() != null) {
            payload.put("expire_timestamp", String.valueOf(pin.getExpire_timestamp().getTime() + pin.getExpire_time().getTime()));
        } else if (pin.getExpire_time() != null) {
            payload.put("expire_timestamp", String.valueOf(pin.getExpire_timestamp().getTime()));
        }

        this.getRESTResponse(url, payload);
        return "redirect:/pins/list";
    }

    /**
     * Method for displaying PINs currently in the database as a table
     */
    @RequestMapping(value = "pins/list", method = RequestMethod.GET)
    public String showCredentials(Model model) {
        List<Pin> pins = pinRepository.findAll();
        model.addAttribute("pins", pins);
        PinDTO pin = new PinDTO();
        pin.setExpire_timestamp(Timestamp.valueOf(LocalDateTime.now().plusDays(2)));
        model.addAttribute("pin", pin);

        return "pin-table";
    }

    /**
     * Translates and passes claim request to the actual REST endpoint
     *
     * @param account
     * @param user
     * @param pin
     * @param request
     */
    @RequestMapping(value = "pins/claim/{account}/{pin}/{user}")
    public String claim(@PathVariable String account, @PathVariable String user, @PathVariable String pin, HttpServletRequest request) {

        Map<String, String> payload = new HashMap<>();
        payload.put("user", user);
        String baseUrl = String.format("%s://%s:%d/api/", request.getScheme(), request.getServerName(), request.getServerPort());
        String url = baseUrl + account + "/" + pin;
        this.getRESTResponse(url, payload);
        return "redirect:/pins/list";
    }

    /**
     * Simply deletes the oid given in path from the database
     *
     * @param oid
     */
    @RequestMapping(value = "pins/delete/{oid}")
    public String delete(@PathVariable Integer oid) {
        pinRepository.delete(oid);
        return "redirect:/pins/list";
    }

    /**
     * Utility method for passing requests to the REST endpoints
     *
     * @param url    URL of the PinRestController endpoint
     * @param params A map of the JSON that would be passed to the endpoint
     */
    private String getRESTResponse(String url, Map<String, String> params) {

        RestTemplate template = new RestTemplate();
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(params);
        String response;
        try {

            ResponseEntity<String> responseEntity = template.exchange(url, HttpMethod.POST, requestEntity, String.class);
            response = responseEntity.getBody();
        } catch (Exception e) {
            response = e.getMessage();
        }
        logger.info(response);
        return response;
    }

}
