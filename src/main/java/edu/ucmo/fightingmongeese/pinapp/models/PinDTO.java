package edu.ucmo.fightingmongeese.pinapp.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalTime;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public LocalDate getExpire_date() {
        return expire_date;
    }

    public void setExpire_date(LocalDate expire_date) {
        this.expire_date = expire_date;
    }

    public LocalTime getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(LocalTime expire_time) {
        this.expire_time = expire_time;
    }
}
