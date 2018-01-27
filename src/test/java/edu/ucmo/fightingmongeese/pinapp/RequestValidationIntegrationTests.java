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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
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
@PropertySource(value = "classpath:ValidationMessages.properties")
public class RequestValidationIntegrationTests {

    /**
     * Field Validation Error Messages
     */
    @Value("${edu.ucmo.fightingmongeese.defaultPINRequiredMessage}")
    String pinRequired;
    @Value("${edu.ucmo.fightingmongeese.defaultUnclaimedMessage}")
    String unclaimed;
    @Value("${edu.ucmo.fightingmongeese.defaultCheckPinExistsMessage}")
    String checkPinExists;
    @Value("${edu.ucmo.fightingmongeese.defaultAccountFormatMessage}")
    String accountFormat;
    @Value("${edu.ucmo.fightingmongeese.defaultSingleActivePinMessage}")
    String singleActivePin;
    @Value("${edu.ucmo.fightingmongeese.defaultAccountRequiredMessage}")
    String accountRequired;
    @Value("${edu.ucmo.fightingmongeese.defaultCreateUserRequiredMessage}")
    String createUserRequired;
    @Value("${edu.ucmo.fightingmongeese.defaultClaimUserRequiredMessage}")
    String claimUserRequired;
    @Value("${edu.ucmo.fightingmongeese.defaultClaimBeforeExpirationMessage}")
    String claimBeforeExpiration;
    @Value("${edu.ucmo.fightingmongeese.defaultExpireTimeMessage}")
    String expireTime;


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

        performRequest(pin, "/api/new", containsInAnyOrder(accountRequired));

        verifyZeroInteractions(pinService);
        verifyZeroInteractions(pinController);
    }

    @Test
    public void test_add_validator_fails_on_missing_claim_user() throws Exception {
        when(pinRepository.findByPin(any(String.class))).thenReturn(Optional.empty());
        Pin pin = new Pin();
        pin.setAccount("account");

        performRequest(pin, "/api/new", containsInAnyOrder(createUserRequired));

        verifyZeroInteractions(pinService);
        verifyZeroInteractions(pinController);
    }


    @Test
    public void test_add_validator_fails_on_invalid_account() throws Exception {
        when(pinRepository.findByPin(any(String.class))).thenReturn(Optional.empty());
        Pin pin = getNewPin();
        pin.setAccount(" account-");

        performRequest(pin, "/api/new", containsInAnyOrder(accountFormat));

        verifyZeroInteractions(pinService);
        verifyZeroInteractions(pinController);
    }

    @Test
    public void test_add_validator_fails_on_active_pin() throws Exception {
        when(pinRepository.findByPin(any(String.class))).thenReturn(Optional.empty());
        when(pinRepository.findActivePin(any(String.class))).thenReturn(Optional.of(getClaimDBResponse()));
        Pin pin = getNewPin();

        performRequest(pin, "/api/new", containsInAnyOrder(singleActivePin));

        verifyZeroInteractions(pinService);
        verifyZeroInteractions(pinController);
    }


    @Test
    public void test_claim_fails_with_missing_user() throws Exception {

        when(pinRepository.findByPin(any(String.class))).thenReturn(Optional.of(getClaimDBResponse()));
        Pin pin = getClaimPin();
        pin.setClaim_user(null);

        performRequest(pin, "/api/claim", containsInAnyOrder(claimUserRequired));

        verifyZeroInteractions(pinService);
        verifyZeroInteractions(pinController);
    }


    @Test
    public void test_claim_fails_with_missing_pin() throws Exception {

        when(pinRepository.findByPin(any(String.class))).thenReturn(Optional.of(getCompletePin()));
        Pin pin = getClaimPin();
        pin.setPin(null);

        performRequest(pin, "/api/claim", containsInAnyOrder(pinRequired));

        verifyZeroInteractions(pinService);
        verifyZeroInteractions(pinController);
    }


    @Test
    public void test_claim_fails_with_pin_not_in_db() throws Exception {

        when(pinRepository.findByPin(any(String.class))).thenReturn(Optional.empty());
        Pin pin = getClaimPin();

        performRequest(pin, "/api/claim", containsInAnyOrder(checkPinExists));

        verifyZeroInteractions(pinService);
        verifyZeroInteractions(pinController);
    }

    @Test
    public void test_claim_fails_with_already_claimed_pin() throws Exception {
        when(pinRepository.findByPin(any())).thenReturn(Optional.of(getCompletePin()));
        Pin pin = getClaimPin();

        performRequest(pin, "/api/claim", containsInAnyOrder(unclaimed));

        verifyZeroInteractions(pinService);
        verifyZeroInteractions(pinController);
    }

    @Test
    public void test_cancel_fails_with_missing_pin() throws Exception {

        when(pinRepository.findByPin(any(String.class))).thenReturn(Optional.of(getCompletePin()));
        Pin pin = getClaimPin();
        pin.setPin(null);

        performRequest(pin, "/api/cancel", containsInAnyOrder(pinRequired));

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

        performRequest(pin, "/api/cancel", containsInAnyOrder(claimUserRequired));

        verifyZeroInteractions(pinService);
        verifyZeroInteractions(pinController);
    }
}
