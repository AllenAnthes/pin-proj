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

    @Autowired
    private PinRepository pinRepository;


    private static final Logger logger = Logger.getLogger(ApplicationStartup.class.getName());

    /**
     * This event is executed as late as conceivably possible to indicate that
     * the application is ready to service requests.
     */
    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {

        SecureRandom random = new SecureRandom();

        Pin pin = new Pin("Bob", "192.168.0.22", "admin");
        int randomPin = random.nextInt(1000000);
        String pinString = String.format("%05d", randomPin);
        pin.setPin(pinString);
        pin.setCreate_timestamp(LocalDateTime.now());
        pin.setExpire_timestamp(LocalDateTime.now().plusDays(2));
        pinRepository.save(pin);

        pin = new Pin("Sally", "192.168.0.35", "user");
        randomPin = random.nextInt(1000000);
        pinString = String.format("%05d", randomPin);
        pin.setPin(pinString);
        pin.setCreate_timestamp(LocalDateTime.now());
        pin.setExpire_timestamp(LocalDateTime.now().plusDays(2));
        pinRepository.save(pin);
    }


} // class
