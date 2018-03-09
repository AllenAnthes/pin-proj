package edu.ucmo.fightingmongeese.pinapp.services;


import edu.ucmo.fightingmongeese.pinapp.components.DateTime;
import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import edu.ucmo.fightingmongeese.pinapp.repository.PinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.stream.IntStream;

/**
 * Primary class containing the business logic behind the REST API
 * <p>
 * As validation is completed before the request arrives at this point
 * the method simply updates the PIN in the repository with the necessary
 * information.
 * <p>
 * Currently there is no authentication implemented and the service assumes any request
 * received is valid.
 * <p>
 * Old record scavenging is not currently implemented but could easily be added.
 */
@Service
public class PinService {

    private final PinRepository pinRepository;

    private final DateTime dateTime;

    private final SecureRandom random;


    @Autowired
    public PinService(PinRepository pinRepository, DateTime dateTime) {
        this.pinRepository = pinRepository;
        this.dateTime = dateTime;
        this.random =  new SecureRandom();
        // TODO: !! DON'T LET THIS GO TO PROD WITH THE SEED !!
        random.setSeed("totessecure".getBytes());

    }

    /**
     * Method containing the business logic for adding a new PIN to the database.
     * A 6 digit PIN is created using Java's SecureRandom class
     *
     * @param pin PIN received from the client to be added to the database
     */
    public Pin add(Pin pin) {
        boolean notUnique;
        String pinString;

        do {
            int randomPin = random.nextInt(1000000);
            pinString = String.format("%06d", randomPin);
            pinString = Pin.getPinWithChecksum(pinString);
//            pinString = getChecksum(pinString);
            notUnique = pinRepository.findByPin(pinString).isPresent();
        } while (notUnique);

        pin.setPin(pinString);
        pin.setCreate_timestamp(dateTime.now());

        if (pin.getExpire_timestamp() == null) {
            // Default expire time set to 30 minutes
            pin.setExpire_timestamp(dateTime.now().plusMinutes(30));
        }
        pin = this.pinRepository.save(pin);
        return pin;
    }

//    private String getChecksum(String pinString) {
//        int sum = getSum(pinString);
//        int checksumDigit = (sum * 9) % 10;
//        String result = pinString + checksumDigit;
//        return result;
//    }
//
//    public static int getSum(String pinString) {
////        int[] multiplier = {pinString.length() % 2 == 0 ? 2 : 1};
//        int[] multiplier ={2};
//        int sum = pinString.chars()
//                .limit(6)
//                .map(i -> i - '0')
//                .map(n -> n * (multiplier[0] = multiplier[0] == 1 ? 2 : 1))
//                .map(n -> n > 9 ? n - 9 : n)
//                .sum();
//
//        // 7 2 6 8 2 3
////        int[] otherMultiplier = {pinString.length() % 2 == 0 ? 2 : 1};
//        int[] otherMultiplier ={2};
//        Integer[] thing = pinString.chars()
//                .limit(6)
//                .map(i -> i - '0')
//                .map(n -> n * (otherMultiplier[0] = otherMultiplier[0] == 1 ? 2 : 1))
//                .map(n -> n > 9 ? n - 9 : n)
//                .boxed()
//                .toArray(Integer[]::new);
//        return sum;
//    }
//
//    public static int newGetSum(String pinString) {
//        StringBuilder sb = new StringBuilder(pinString);
//        int sum = 0;
//        for (int i = 0; i < sb.length(); i++) {
//            int nextDigit = Integer.parseInt(sb.substring(i, i + 1));
//            if (i % 2 == 1) {
//                nextDigit *= 2;
//                if (nextDigit > 9) {
//                    nextDigit -= 9;
//                }
//            }
//            sum += nextDigit;
//        }
//        return sum;
//    }

    /**
     * Method for handling claims sent to the REST API.
     *
     * @param claimPin PIN sent from the client to be claimed.
     */
    public Pin claim(Pin claimPin) {
        Pin pin = pinRepository.findByPin(claimPin.getPin()).orElse(new Pin());
        pin.setClaim_user(claimPin.getClaim_user());
        pin.setClaim_ip(claimPin.getClaim_ip());
        pin.setClaim_timestamp(dateTime.now());
        return pinRepository.save(pin);
    }

    /**
     * Method used to interact with the database to cancel an active PIN
     *
     * @param cancelPin PIN sent from the client to be canceled.
     */
    public Pin cancel(Pin cancelPin) {

        Pin pin = pinRepository.findByPin(cancelPin.getPin()).orElse(new Pin());
        pin.setClaim_ip(cancelPin.getClaim_ip());
        pin.setClaim_user(cancelPin.getClaim_user());
        pin.setClaim_timestamp(dateTime.now());
        return pinRepository.save(pin);
    }
}
