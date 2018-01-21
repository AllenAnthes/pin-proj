package edu.ucmo.fightingmongeese.pinapp.models;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Model used as a container for simplifying interaction with the WebGUI.
 */
@Data
@Entity
public class PinDTO {

    @Id
    private Long id;

    @Column
    private String account;

    @Column
    private String pin;

    @Column
    private String claim_user;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expire_date;

    @Column
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime expire_time;

    @Column
    private String claim_ip;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime claim_timestamp;


    public PinDTO() {
    }

}
