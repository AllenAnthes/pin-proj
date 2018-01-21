package edu.ucmo.fightingmongeese.pinapp;

import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import edu.ucmo.fightingmongeese.pinapp.repository.PinRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PINRepositoryTest {

    @Autowired
    PinRepository pinRepository;


    @Test
    public void whenGetOne_thenReturnPIN() {
        Pin pin = new Pin("Bob", "192.168.0.22", "admin");
        pin.setOid(1);
        pin.setPin("123456");
        pin.setCreate_timestamp(LocalDateTime.now());
        pin.setExpire_timestamp(LocalDateTime.now().plusDays(2));
        pinRepository.save(pin);


        Pin found = pinRepository.findOne(1);

        assertThat(found.getAccount())
                .isEqualTo(pin.getAccount());
    }


}
