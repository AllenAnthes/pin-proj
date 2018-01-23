package edu.ucmo.fightingmongeese.pinapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ucmo.fightingmongeese.pinapp.controllers.PinController;
import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import edu.ucmo.fightingmongeese.pinapp.repository.PinRepository;
import edu.ucmo.fightingmongeese.pinapp.services.PinService;
import edu.ucmo.fightingmongeese.pinapp.validators.SingleActivePinValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
//@ActiveProfiles("test")
//@WebAppConfiguration
@TestPropertySource(locations="classpath:test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PinControllerTest {


    private MockMvc mockMvc;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Mock
    private PinService pinService;

    @Mock
    private PinRepository pinRepository;



    @InjectMocks
    private PinController pinController;

    @Autowired
    private WebApplicationContext wac;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
//                .standaloneSetup(pinController)
                .build();
    }

    @Test
    public void test_add_pin_success() throws Exception {
        Pin pin = getNewPin();
        when(pinService.add(pin, pin.getCreate_ip())).thenReturn(getCompletePin());
//        PinRepository pr = Mockito.mock(SingleActivePinValidator.pinRepository.findActivePin(any()));
//        when(pinRepository.findActivePin(any())).thenReturn(Optional.empty());
//        mockMvc.perform(
//                post("/api/new")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(pin)))
//                .andExpect(status().isOk());
//        pinController.add(pin, )
        System.out.println("");
        verify(pinService, times(1)).add(pin, pin.getCreate_ip());
    }

    @Test
    public void test_claim_pin_success() throws Exception {
        Pin pin = getClaimPin();
        when(pinService.claim(pin, pin.getClaimIp())).thenReturn(getCompletePin());
        mockMvc.perform(
                post("/api/claim")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(pin)))
                .andExpect(status().isOk());
        verify(pinService, times(1)).claim(pin, pin.getClaimIp());
    }

    @Test
    public void test_cancel_pin_success() throws Exception {
        Pin pin = getClaimPin();
        when(pinService.cancel(pin, pin.getClaimIp())).thenReturn(getCompletePin());
        mockMvc.perform(
                post("/api/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(pin)))
                .andExpect(status().isOk());
        verify(pinService, times(1)).cancel(pin, pin.getClaimIp());
    }


    private static Pin getNewPin() {
        return new Pin("SallysSavings2", "127.0.0.1", "sally");
    }


    private static Pin getClaimPin() {
        Pin pin = new Pin();
        pin.setPin("123456");
        pin.setClaim_user("Bob");
        pin.setClaimIp("127.0.0.1");
        return pin;
    }

    private static Pin getCompletePin() {
        Pin pin = new Pin("SallysSavings", "127.0.0.1", "sally");
        pin.setPin("123456");
        pin.setClaim_user("Bob");
        pin.setClaimIp("127.0.0.1");
        return pin;
    }


    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
