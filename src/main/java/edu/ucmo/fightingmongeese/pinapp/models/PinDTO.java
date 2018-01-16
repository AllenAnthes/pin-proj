package edu.ucmo.fightingmongeese.pinapp.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Time;
import java.util.Date;

@Entity
public class PinDTO {

    @Id
    private Long id;

    @Column
    private String account;

    @Column
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date expire_timestamp;

    @Column
    @DateTimeFormat(pattern = "hh:mm:ss")
    private Date expire_time;

//    @Column
//    private

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

    public Date getExpire_timestamp() {
        return expire_timestamp;
    }

    public void setExpire_timestamp(Date expire_timestamp) {
        this.expire_timestamp = expire_timestamp;
    }

    public Date getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(Date expire_time) {
        this.expire_time = expire_time;
    }
}
