package edu.ucmo.fightingmongeese.pinapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ucmo.fightingmongeese.pinapp.models.Pin;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

class TestUtils {


    private static final LocalDateTime mockedTime = LocalDateTime.ofEpochSecond(10000, 0, ZoneOffset.UTC);

    private static String IPV4_LOCALHOST = "127.0.0.1";



    static Pin getCompletePin() {
        Pin pin = new Pin("SallysChecking", "127.0.0.1", "sally");
        pin.setPin("123456");
        pin.setClaim_user("Bob");
        pin.setClaim_ip("127.0.0.1");
        return pin;
    }


    static Pin getNewPin() {
        return new Pin("SallysSavings", IPV4_LOCALHOST, "sally");
    }

    static Pin getCancelPin() {
        Pin pin = new Pin();
        pin.setPin("123456");
        pin.setClaim_ip(IPV4_LOCALHOST);
        return pin;
    }

    static Pin getClaimPin() {
        Pin pin = new Pin();
        pin.setClaim_ip(IPV4_LOCALHOST);
        pin.setPin("123456");
        pin.setClaim_user("bob");
//        pin.setExpire_timestamp(mockedTime.plusHours(5));
        return pin;
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
