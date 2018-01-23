package edu.ucmo.fightingmongeese.pinapp.components;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public interface DateTime {
    LocalDateTime now();
}
