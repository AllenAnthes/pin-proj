package edu.ucmo.fightingmongeese.pinapp.controllers;


import edu.ucmo.fightingmongeese.pinapp.filters.ExpireDateFilter;
import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import edu.ucmo.fightingmongeese.pinapp.models.PinDTO;
import edu.ucmo.fightingmongeese.pinapp.repository.PinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyEditorSupport;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
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


    @RequestMapping(value = "pins/new", method = RequestMethod.POST)
    public String add(@ModelAttribute PinDTO pin, Map<String, String> JSON, HttpServletRequest request) {

        String baseUrl = String.format("%s://%s:%d/api/", request.getScheme(), request.getServerName(), request.getServerPort());
        String url = baseUrl + "/new";

        System.out.println(JSON);

        Map<String, String> payload = new HashMap<>();
        payload.put("account", pin.getAccount());
        payload.put("create_user", request.getUserPrincipal().getName());
        if (pin.getExpire_timestamp() != null && pin.getExpire_time() != null) {
            payload.put("expire_timestamp", String.valueOf(pin.getExpire_timestamp().getTime() + pin.getExpire_time().getTime()));
        }


        this.getRESTResponse(url, payload);
        return "redirect:/pins/list";
    }


    @RequestMapping(value = "pins/list", method = RequestMethod.GET)
    public String showCredentials(Model model) {
        List<Pin> pins = pinRepository.findAll();
        model.addAttribute("filter", new ExpireDateFilter());
        model.addAttribute("pins", pins);
//        Pin pin = new Pin();
        PinDTO pin = new PinDTO();
        pin.setExpire_timestamp(Timestamp.valueOf(LocalDateTime.now().plusDays(2)));
        model.addAttribute("pin", pin);

        return "pin-table";
    }

    @RequestMapping(value = "pins/claim/{account}/{pin}/{user}")
    public String claim(@PathVariable String account, @PathVariable String user, @PathVariable String pin, HttpServletRequest request) {

        Map<String, String> payload = new HashMap<>();
        payload.put("user", user);
        String baseUrl = String.format("%s://%s:%d/api/", request.getScheme(), request.getServerName(), request.getServerPort());
        String url = baseUrl + account + "/" + pin;
        this.getRESTResponse(url, payload);
        return "redirect:/pins/list";
    }

    @RequestMapping(value = "pins/delete/{oid}")
    public String delete(@PathVariable Integer oid) {
        pinRepository.delete(oid);
        return "redirect:/pins/list";
    }

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
