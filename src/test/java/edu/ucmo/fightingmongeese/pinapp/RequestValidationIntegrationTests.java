package edu.ucmo.fightingmongeese.pinapp;


import edu.ucmo.fightingmongeese.pinapp.components.DateTime;
import edu.ucmo.fightingmongeese.pinapp.controllers.PinController;
import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import edu.ucmo.fightingmongeese.pinapp.repository.PinRepository;
import edu.ucmo.fightingmongeese.pinapp.services.PinService;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static edu.ucmo.fightingmongeese.pinapp.TestUtils.*;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = PinController.class)
@ComponentScan("edu.ucmo.fightingmongeese.pinapp")
public class RequestValidationIntegrationTests {


    private MockMvc mockMVc;

    @Autowired
    private WebApplicationContext wac;

    @MockBean
    private PinService pinService;

    @MockBean
    private PinRepository pinRepository;

    @Mock
    private DateTime dateTime;

    @Spy
    @InjectMocks
    private PinController pinController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMVc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();
        when(pinRepository.findActivePin(any(String.class))).thenReturn(Optional.empty());
        when(dateTime.now()).thenReturn(mockedTime);
    }

    @Test
    public void test_add_validator_fails_on_missing_accout() throws Exception {
        when(pinRepository.findByPin(any(String.class))).thenReturn(Optional.empty());
        Pin pin = new Pin();
        pin.setCreate_user("user");

        performRequest(pin, "/api/new", containsInAnyOrder("Account must be provided in request"));

        verifyZeroInteractions(pinService);
        verifyZeroInteractions(pinController);
    }

    @Test
    public void test_add_validator_fails_on_missing_claim_user() throws Exception {
        when(pinRepository.findByPin(any(String.class))).thenReturn(Optional.empty());
        Pin pin = new Pin();
        pin.setAccount("account");

        performRequest(pin, "/api/new", containsInAnyOrder("New PINs must supply a create_user"));

        verifyZeroInteractions(pinService);
        verifyZeroInteractions(pinController);
    }


    @Test
    public void test_add_validator_fails_on_invalid_account() throws Exception {
        when(pinRepository.findByPin(any(String.class))).thenReturn(Optional.empty());
        Pin pin = getNewPin();
        pin.setAccount(" account-");

        performRequest(pin, "/api/new", containsInAnyOrder("New PINs must supply an alphanumeric account"));

        verifyZeroInteractions(pinService);
        verifyZeroInteractions(pinController);
    }

    @Test
    public void test_add_validator_fails_on_active_pin() throws Exception {
        when(pinRepository.findByPin(any(String.class))).thenReturn(Optional.empty());
        when(pinRepository.findActivePin(any(String.class))).thenReturn(Optional.of(getClaimDBResponse()));
        Pin pin = getNewPin();

        performRequest(pin, "/api/new", containsInAnyOrder("An account can only have one PIN active at a time."));

        verifyZeroInteractions(pinService);
        verifyZeroInteractions(pinController);
    }


    @Test
    public void test_claim_fails_with_missing_user() throws Exception {

        when(pinRepository.findByPin(any(String.class))).thenReturn(Optional.of(getClaimDBResponse()));
        Pin pin = getClaimPin();
        pin.setClaim_user(null);

        performRequest(pin, "/api/claim", containsInAnyOrder("Claim user is required"));

        verifyZeroInteractions(pinService);
        verifyZeroInteractions(pinController);
    }


    @Test
    public void test_claim_fails_with_missing_pin() throws Exception {

        when(pinRepository.findByPin(any(String.class))).thenReturn(Optional.of(getCompletePin()));
        Pin pin = getClaimPin();
        pin.setPin(null);

        performRequest(pin, "/api/claim", containsInAnyOrder("PIN must be provided in request"));

        verifyZeroInteractions(pinService);
        verifyZeroInteractions(pinController);
    }


    @Test
    public void test_claim_fails_with_pin_not_in_db() throws Exception {

        when(pinRepository.findByPin(any(String.class))).thenReturn(Optional.of(getCompletePin()));
        Pin pin = getClaimPin();

        performRequest(pin, "/api/claim", containsInAnyOrder("Claim submitted on previously claimed PIN"));

        verifyZeroInteractions(pinService);
        verifyZeroInteractions(pinController);
    }

    @Test
    public void test_claim_fails_with_already_claimed_pin() throws Exception {
        when(pinRepository.findByPin(any())).thenReturn(Optional.of(getCompletePin()));
        Pin pin = getClaimPin();

        performRequest(pin, "/api/claim", containsInAnyOrder("Claim submitted on previously claimed PIN"));

        verifyZeroInteractions(pinService);
        verifyZeroInteractions(pinController);
    }

    @Test
    public void test_cancel_fails_with_missing_pin() throws Exception {

        when(pinRepository.findByPin(any(String.class))).thenReturn(Optional.of(getCompletePin()));
        Pin pin = getClaimPin();
        pin.setPin(null);

        performRequest(pin, "/api/cancel", containsInAnyOrder("PIN must be provided in request"));

        verifyZeroInteractions(pinService);
        verifyZeroInteractions(pinController);
    }


    private void performRequest(Pin pin, String url, Matcher<Iterable<? extends String>> matcher) throws Exception {
        mockMVc.perform(post(url)
                .content(asJsonString(pin))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[*].message", matcher))
                .andDo(print());
    }

    @Test
    public void test_cancel_fails_with_missing_user() throws Exception {

        when(pinRepository.findByPin(any(String.class))).thenReturn(Optional.of(getClaimDBResponse()));
        Pin pin = getClaimPin();
        pin.setClaim_user(null);

        performRequest(pin, "/api/cancel", containsInAnyOrder("Claim user is required"));

        verifyZeroInteractions(pinService);
        verifyZeroInteractions(pinController);
    }
}
