package edu.ucmo.fightingmongeese.pinapp.components;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 *  Component is necessary in order to create tests
 *  that can be mocked and are not dependant on
 *  system time
 */
@Component
public class LocalDateTimeImpl implements DateTime {
    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
