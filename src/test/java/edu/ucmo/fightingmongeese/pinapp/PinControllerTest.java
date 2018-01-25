package edu.ucmo.fightingmongeese.pinapp;

import edu.ucmo.fightingmongeese.pinapp.controllers.PinController;
import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import edu.ucmo.fightingmongeese.pinapp.repository.PinRepository;
import edu.ucmo.fightingmongeese.pinapp.services.PinService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;

import static edu.ucmo.fightingmongeese.pinapp.TestUtils.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(SpringRunner.class)
public class PinControllerTest {

    @MockBean
    private PinService pinService;

    @MockBean
    private PinRepository pinRepository;

    @MockBean
    private HttpServletRequest mockedRequest;

    @InjectMocks
    private PinController pinController;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void test_add_pin_success() {
        HttpServletRequest mockedRequest = Mockito.mock(HttpServletRequest.class);
        Pin pin = getNewPin();
        when(pinService.add(any(Pin.class))).thenReturn(pin);

        pinController.add(pin, mockedRequest);

        verify(pinService, times(1)).add(pin);
    }

    @Test
    public void test_claim_pin_success() {
        Pin pin = getClaimPin();
        when(pinService.claim(pin)).thenReturn(getCompletePin());

        pinController.claim(pin, mockedRequest);


        verify(pinService, times(1)).claim(pin);
    }

    @Test
    public void test_cancel_pin_success() {
        Pin pin = getClaimPin();
        when(pinService.cancel(pin)).thenReturn(getCompletePin());

        pinController.cancel(pin, mockedRequest);

        verify(pinService, times(1)).cancel(pin);
    }
}
