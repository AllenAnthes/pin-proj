package edu.ucmo.fightingmongeese.pinapp;

import edu.ucmo.fightingmongeese.pinapp.components.DateTime;
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
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(SpringRunner.class)
public class PinServiceTest {

    @Mock
    private PinRepository pinRepository;

    @Mock
    private DateTime dateTime;

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

    @SuppressWarnings("unchecked")
    @Test
    public void test_add_pin_rerolls_used_pin() {
        Pin pin = getNewPin();
        when(pinRepository.findActivePin(any())).thenReturn(Optional.empty());
        when(pinRepository.findByPin(any())).thenReturn(Optional.of(new Pin()), Optional.empty());
        when(pinRepository.save(pin)).thenReturn(new Pin("123", "123"));

        pin = pinService.add(pin, pin.getCreate_ip());

        verify(pinRepository, times(2)).findByPin(any());
        assertEquals(pin, new Pin("123", "123"));
    }

    @Test
    public void test_add_pin_does_not_overwrite_given_expiration() {
        Pin pin = getNewPin();
        pin.setExpire_timestamp(LocalDateTime.now().plusDays(1));

        when(pinRepository.findActivePin(any())).thenReturn(Optional.empty());
        when(pinRepository.findByPin(any())).thenReturn((Optional.empty()));
        when(pinRepository.save(pin)).thenReturn(pin);

        Pin result = pinService.add(pin, pin.getCreate_ip());

        assertEquals(result.getExpire_timestamp(), pin.getExpire_timestamp());
        verify(pinRepository, times(1)).findByPin(pin.getPin());
        verify(pinRepository, times(1)).save(pin);
        verify(pinRepository, times(1)).findActivePin(any());
    }


    @Test
    public void test_add_pin_adds_default_expiration() {
        LocalDateTime mockedTime = LocalDateTime.ofEpochSecond(10000,0,ZoneOffset.UTC);

        Pin pin = getNewPin();

        when(pinRepository.findActivePin(any())).thenReturn(Optional.empty());
        when(pinRepository.findByPin(any())).thenReturn((Optional.empty()));
        when(pinRepository.save(pin)).thenReturn(pin);
        when(dateTime.now()).thenReturn(mockedTime);

        Pin result = pinService.add(pin, pin.getCreate_ip());


        assertEquals(result.getExpire_timestamp(), mockedTime.plusMinutes(30));

        verify(pinRepository, times(1)).findByPin(pin.getPin());
        verify(pinRepository, times(1)).save(pin);
        verify(pinRepository, times(1)).findActivePin(any());

    }


    private static Pin getNewPin() {
        return new Pin("SallysSavings", "127.0.0.1", "sally");
    }

}
