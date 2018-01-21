package edu.ucmo.fightingmongeese.pinapp;

import edu.ucmo.fightingmongeese.pinapp.exceptions.AccountHasActivePinException;
import edu.ucmo.fightingmongeese.pinapp.exceptions.InvalidExpireTimeException;
import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import edu.ucmo.fightingmongeese.pinapp.repository.PinRepository;
import edu.ucmo.fightingmongeese.pinapp.services.PinService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(SpringRunner.class)
public class PinServiceTest {

    @Mock
    private PinRepository pinRepository;


    @InjectMocks
    private PinService pinService;


    @Test(expected = InvalidExpireTimeException.class)
    public void test_add_pin_with_invalid_expiration() {
        Pin pin = getNewPin();
        pin.setExpire_timestamp(LocalDateTime.MIN);
        pinService.add(pin, pin.getCreate_ip());
    }

    @Test(expected = AccountHasActivePinException.class)
    public void test_add_pin_with_active_pin() {
        Pin pin = getNewPin();
        when(pinRepository.findActivePin(any())).thenReturn(Optional.of(new Pin()));
        pinService.add(pin, pin.getCreate_ip());
    }

    @Test
    public void test_add_pin_with_valid_args() {
        Pin pin = getNewPin();
        when(pinRepository.findByPin(any())).thenReturn(Optional.empty());
        when(pinRepository.findActivePin(any())).thenReturn(Optional.empty());
        when(pinRepository.save(pin)).thenReturn(new Pin());
        Pin result = pinService.add(pin, pin.getCreate_ip());
        assertEquals(result, new Pin());
        verify(pinRepository, times(1)).findByPin(pin.getPin());
        verify(pinRepository, times(1)).save(pin);
        verify(pinRepository, times(1)).findActivePin(any());
        verifyNoMoreInteractions(pinRepository);
    }


    private static Pin getNewPin() {
        return new Pin("SallysSavings", "127.0.0.1", "sally");
    }

}
