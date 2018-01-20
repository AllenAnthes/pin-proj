package edu.ucmo.fightingmongeese.pinapp.config;

import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import edu.ucmo.fightingmongeese.pinapp.repository.PinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.logging.Logger;

@Component
public class ApplicationStartup
        implements ApplicationListener<ApplicationReadyEvent> {

    private final PinRepository pinRepository;


    private static final Logger logger = Logger.getLogger(ApplicationStartup.class.getName());

    @Autowired
    public ApplicationStartup(PinRepository pinRepository) {
        this.pinRepository = pinRepository;
    }

    /**
     * This event is executed as late as conceivably possible to indicate that
     * the application is ready to service requests.
     */
    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {

        SecureRandom random = new SecureRandom();

        random.setSeed("totessecure".getBytes());

        Pin pin = new Pin("Bob's Checking", "192.168.0.22", "bob");
        int randomPin = random.nextInt(1000000);
        String pinString = String.format("%06d", randomPin);
        pin.setPin(pinString);
        pin.setCreate_timestamp(LocalDateTime.now());
        pin.setExpire_timestamp(LocalDateTime.now().plusMinutes(30));
        pinRepository.save(pin);

        pin = new Pin("Sally's Savings", "192.168.0.35", "sally");
        randomPin = random.nextInt(1000000);
        pinString = String.format("%06d", randomPin);
        pin.setPin(pinString);
        pin.setCreate_timestamp(LocalDateTime.now());
        pin.setExpire_timestamp(LocalDateTime.now().plusMinutes(30));
        pinRepository.save(pin);


        pin = new Pin("BobsSavings", "192.168.0.35", "bob");
        int num = 1234;
        pinString = String.format("%06d", num);
        pin.setPin(pinString);
        pin.setCreate_timestamp(LocalDateTime.now());
        pin.setExpire_timestamp(LocalDateTime.now().plusMinutes(30));
        pinRepository.save(pin);
    }


}
