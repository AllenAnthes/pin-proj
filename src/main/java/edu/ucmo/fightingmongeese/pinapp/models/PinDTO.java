package edu.ucmo.fightingmongeese.pinapp.models;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
public class PinDTO {

    @Id
    private Long id;

    @Column
    private String account;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expire_date;

    @Column
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime expire_time;


    public PinDTO() {
    }

}
