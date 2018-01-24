package edu.ucmo.fightingmongeese.pinapp;

import edu.ucmo.fightingmongeese.pinapp.components.DateTime;
import edu.ucmo.fightingmongeese.pinapp.exceptions.*;
import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import edu.ucmo.fightingmongeese.pinapp.repository.PinRepository;
import edu.ucmo.fightingmongeese.pinapp.services.PinService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static edu.ucmo.fightingmongeese.pinapp.TestUtils.getCancelPin;
import static edu.ucmo.fightingmongeese.pinapp.TestUtils.getClaimPin;
import static edu.ucmo.fightingmongeese.pinapp.TestUtils.getNewPin;
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

    private final LocalDateTime mockedTime = LocalDateTime.ofEpochSecond(10000, 0, ZoneOffset.UTC);

    private String IPV4_LOCALHOST = "127.0.0.1";


    @Before
    public void setupMock() {
        when(dateTime.now()).thenReturn(mockedTime);
    }

    // TODO: Commented out tests are being migrated to validator tests

//    @Test(expected = InvalidExpireTimeException.class)
//    public void test_add_pin_with_invalid_expiration() {
//        Pin pin = getNewPin();
//        pin.setExpire_timestamp(LocalDateTime.MIN);
//        when(pinRepository.findByPin(any())).thenReturn(Optional.empty());
//        pinService.add(pin, pin.getCreate_ip());
//    }


//    @Test(expected = AccountHasActivePinException.class)
//    public void test_add_pin_with_active_pin() {
//        Pin pin = getNewPin();
//        when(pinRepository.findActivePin(any())).thenReturn(Optional.of(new Pin()));
//        pinService.add(pin, pin.getCreate_ip());
//    }

    @Test
    public void test_add_pin_with_valid_args() {
        Pin pin = getNewPin();
        when(pinRepository.findByPin(any())).thenReturn(Optional.empty());
        when(pinRepository.findActivePin(any())).thenReturn(Optional.empty());
        when(pinRepository.save(pin)).thenReturn(new Pin());

        Pin result = pinService.add(pin);

        assertEquals(result, new Pin());
        verify(pinRepository, times(1)).findByPin(pin.getPin());
        verify(pinRepository, times(1)).save(pin);
//        verify(pinRepository, times(1)).findActivePin(any());
        verifyNoMoreInteractions(pinRepository);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test_add_pin_rerolls_used_pin() {
        Pin pin = getNewPin();
        when(pinRepository.findActivePin(any())).thenReturn(Optional.empty());
        when(pinRepository.findByPin(any())).thenReturn(Optional.of(new Pin()), Optional.empty());
        when(pinRepository.save(pin)).thenReturn(new Pin("123", "123"));

        pin = pinService.add(pin);

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

        Pin result = pinService.add(pin);

        assertEquals(result.getExpire_timestamp(), pin.getExpire_timestamp());
        verify(pinRepository, times(1)).findByPin(pin.getPin());
        verify(pinRepository, times(1)).save(pin);
//        verify(pinRepository, times(1)).findActivePin(any());
    }


    @Test
    public void test_add_pin_adds_default_expiration() {
        Pin pin = getNewPin();

        when(pinRepository.findActivePin(any())).thenReturn(Optional.empty());
        when(pinRepository.findByPin(any())).thenReturn((Optional.empty()));
        when(pinRepository.save(pin)).thenReturn(pin);

        Pin result = pinService.add(pin);

        assertEquals(result.getExpire_timestamp(), mockedTime.plusMinutes(30));
        verify(pinRepository, times(1)).findByPin(pin.getPin());
        verify(pinRepository, times(1)).save(pin);
//        verify(pinRepository, times(1)).findActivePin(any());

    }

//    @Test(expected = PinNotFoundException.class)
//    public void test_claim_throws_exception_with_invalid_pin() {
//        Pin pin = getClaimPin();
//        when(pinRepository.findByPin(any())).thenReturn(Optional.empty());
//
//        pinService.claim(pin, pin.getClaim_ip());
//    }

//    @Test(expected = ExpiredPinException.class)
//    public void test_claim_throws_exception_with_invalid_expiration() {
//        Pin pin = getClaimPin();
//        pin.setExpire_timestamp(LocalDateTime.MIN);
//        when(pinRepository.findByPin(any())).thenReturn(Optional.of(pin));
//
//        pinService.claim(pin, pin.getClaim_ip());
//    }

//    @Test(expected = PinAlreadyClaimedException.class)
//    public void test_claim_throws_exception_with_claimed_pin() {
//        Pin pin = getClaimPin();
//        pin.setClaim_timestamp(mockedTime);
//        when(pinRepository.findByPin(any())).thenReturn(Optional.of(pin));
//
//        pinService.claim(pin, pin.getClaim_ip());
//    }

//    @Test(expected = MissingClaimUserException.class)
//    public void test_claim_throws_exception_with_missing_user() {
//        Pin pin = getClaimPin();
//        pin.setClaim_user(null);
//        when(pinRepository.findByPin(any())).thenReturn(Optional.of(pin));
//
//        pinService.claim(pin, pin.getClaim_ip());
//    }

    @Test
    public void test_claim_sets_claim_timestamp_correctly() {
        Pin pin = getClaimPin();
        pin.setExpire_timestamp(LocalDateTime.MAX);
        when(pinRepository.findByPin(any())).thenReturn(Optional.of(pin));
        when(pinRepository.save(pin)).thenReturn(pin);

        Pin result = pinService.claim(pin);
        assertEquals(result.getClaim_timestamp(), mockedTime);
    }

    @Test
    public void test_cancel_works_with_valid_args() {
        Pin pin = getCancelPin();
        when(pinRepository.findByPin(any())).thenReturn(Optional.of(pin));
        when(pinRepository.save(pin)).thenReturn(pin);

        Pin result = pinService.cancel(pin);

        assertEquals(result.getClaim_timestamp(), mockedTime);
        assertEquals(result.getClaim_ip(), IPV4_LOCALHOST);
        assertEquals(result.getClaim_user(), getCancelPin().getClaim_user());
    }


//    @Test(expected = PinNotFoundException.class)
//    public void test_cancel_throws_exception_with_invalid_pin() {
//        Pin pin = getCancelPin();
//        when(pinRepository.findByPin(any())).thenReturn(Optional.empty());
//
//        pinService.cancel(pin);
//    }
}
