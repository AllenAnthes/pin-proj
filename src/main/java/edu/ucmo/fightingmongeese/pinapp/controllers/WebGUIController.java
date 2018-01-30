package edu.ucmo.fightingmongeese.pinapp.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import edu.ucmo.fightingmongeese.pinapp.models.PinDTO;
import edu.ucmo.fightingmongeese.pinapp.repository.PinRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Temporary Controller for the Thymeleaf based front-end
 * Used for testing and demo purposes
 * Will be removed before prod.  No judging my code here
 */
@Controller
public class WebGUIController {

    @Autowired
    PinRepository pinRepository;

    private static final Logger logger = LoggerFactory.getLogger(PinController.class);

    /**
     * Method for displaying PINs currently in the database as a table.
     * <p>
     * All model attributes are passed to this method when a user performs an action on the frontend
     * and are embedded in the model before the user is redirected back here
     *
     * @param result     result returned from the API call
     * @param payload    payload sent to the REST API
     * @param requestUrl REST API endpoint request was forwarded to
     * @param model      Container for attributes that is sent to the frontend
     * @return pin-table    model and the html page to be displayed to the user
     */
    @RequestMapping(value = "pins/list", method = RequestMethod.GET)
    public String showCredentials(@ModelAttribute("resultAttribute") String result,
                                  @ModelAttribute("payloadAttribute") HashMap<String, String> payload,
                                  @ModelAttribute("requestUrl") String requestUrl, Model model) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        if (!requestUrl.equals("")) {
            model.addAttribute("requestUrl", requestUrl);
        }
        if (!result.equals("")) {
            Object json = mapper.readValue(result, Object.class);
            model.addAttribute("result", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));
        }
        if (!payload.isEmpty()) {
            model.addAttribute("request", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(payload));

        }
        List<Pin> pins = pinRepository.findAll();
        model.addAttribute("pins", pins);
        PinDTO pin = new PinDTO();
        pin.setExpire_date(LocalDate.now().plusDays(2));
        pin.setExpire_time(LocalTime.now());


        model.addAttribute("pin", pin);

        return "pin-table";
    }

    /**
     * Method for routing new submissions from the Thymeleaf UI to the REST endpoint
     *
     * @param pin     DTO that holds the info to be transferred to the actual endpoint.
     *                Had to use a hack to get the Date and Time to properly translate
     * @param request
     */
    @RequestMapping(value = "pins/new", method = RequestMethod.POST)
    public String add(@ModelAttribute PinDTO pin, HttpServletRequest request, RedirectAttributes attributes) {

        String baseUrl = String.format("%s://%s:%d/api/", request.getScheme(),
                request.getServerName(), request.getServerPort());
        String url = baseUrl + "/new";

        Map<String, String> payload = new HashMap<>();
        payload.put("account", pin.getAccount());
        payload.put("create_user", request.getUserPrincipal().getName());

        if (pin.getExpire_date() != null && pin.getExpire_time() != null) {
            payload.put("expire_timestamp", String.valueOf(LocalDateTime.of(pin.getExpire_date(), pin.getExpire_time())));
        } else if (pin.getExpire_date() != null) {
            payload.put("expire_timestamp", String.valueOf(pin.getExpire_date()));
        }

        String result = this.getRESTResponse(url, payload);
        attributes.addFlashAttribute("resultAttribute", result);
        attributes.addFlashAttribute("requestUrl", baseUrl);
        attributes.addFlashAttribute("payloadAttribute", payload);

        return "redirect:/pins/list";
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
    public String claim(@PathVariable String account, @PathVariable String user, @PathVariable String pin,
                        HttpServletRequest request, RedirectAttributes attributes) {

        Map<String, String> payload = new HashMap<>();
        payload.put("claim_user", user);
        payload.put("pin", pin);
        String baseUrl = String.format("%s://%s:%d/api/claim", request.getScheme(), request.getServerName(), request.getServerPort());
        String result = this.getRESTResponse(baseUrl, payload);
        attributes.addFlashAttribute("resultAttribute", result);
        attributes.addFlashAttribute("requestUrl", baseUrl);
        attributes.addFlashAttribute("payloadAttribute", payload);

        return "redirect:/pins/list";
    }

    /**
     * Simply deletes the oid given in path from the database
     *
     * @param oid
     */
    @RequestMapping(value = "pins/delete/{oid}")
    public String delete(@PathVariable Integer oid, HttpServletRequest request) {
        Pin pin = pinRepository.getOne(oid);
        pinRepository.delete(oid);
        logger.info(String.format("PIN deleted: Account: %s | PIN: %s | User: %s | IP: %s",
                pin.getAccount(), pin.getPin(), request.getUserPrincipal().getName(), request.getRemoteAddr()));

        return "redirect:/pins/list";
    }

    /**
     * Development testing mapping for receiving request from web GUI to
     * increase expiration time.
     *
     * @param oid
     * @param request
     * @return
     */
    @RequestMapping(value = "pins/resetExpiration/{oid}")
    public String resetExpiration(@PathVariable Integer oid, HttpServletRequest request, RedirectAttributes attributes) {
        logger.info("Reset expiration request recevied for oid: {}", oid);

        Map<String, String> payload = new HashMap<>();
        payload.put("oid", String.valueOf(oid));

        String baseUrl = String.format("%s://%s:%d/test/resetExpiration", request.getScheme(), request.getServerName(), request.getServerPort());
        String result = this.getRESTResponse(baseUrl, payload);
        attributes.addFlashAttribute("resultAttribute", result);
        attributes.addFlashAttribute("requestUrl", baseUrl);
        attributes.addFlashAttribute("payloadAttribute", payload);

        return "redirect:/pins/list";
    }

    /**
     * Development testing mapping for receiving request from web GUI to
     * reset claim time.
     *
     * @param oid
     * @param request
     * @return
     */
    @RequestMapping(value = "pins/resetClaim/{oid}")
    public String resetClaim(@PathVariable Integer oid, HttpServletRequest request, RedirectAttributes attributes) {
        logger.info("Reset claim request recevied for oid: {}", oid);

        Map<String, String> payload = new HashMap<>();
        payload.put("oid", String.valueOf(oid));

        String baseUrl = String.format("%s://%s:%d/test/unclaim", request.getScheme(), request.getServerName(), request.getServerPort());
        String result = this.getRESTResponse(baseUrl, payload);
        attributes.addFlashAttribute("resultAttribute", result);
        attributes.addFlashAttribute("requestUrl", baseUrl);
        attributes.addFlashAttribute("payloadAttribute", payload);

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
            response = ((HttpClientErrorException) e).getResponseBodyAsString();
//            logger.warn(response);
        }
//        logger.info(response);
        return response;
    }
}
