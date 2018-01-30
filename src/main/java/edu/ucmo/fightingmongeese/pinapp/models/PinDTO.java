package edu.ucmo.fightingmongeese.pinapp.models;

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

    public String getCreate_user() {
        return create_user;
    }

    public void setCreate_user(String create_user) {
        this.create_user = create_user;
    }

    @Column
    private String create_user;

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

    public Long getId() {
        return this.id;
    }

    public String getAccount() {
        return this.account;
    }

    public String getPin() {
        return this.pin;
    }

    public String getClaim_user() {
        return this.claim_user;
    }

    public LocalDate getExpire_date() {
        return this.expire_date;
    }

    public LocalTime getExpire_time() {
        return this.expire_time;
    }

    public String getClaim_ip() {
        return this.claim_ip;
    }

    public LocalDateTime getClaim_timestamp() {
        return this.claim_timestamp;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setClaim_user(String claim_user) {
        this.claim_user = claim_user;
    }

    public void setExpire_date(LocalDate expire_date) {
        this.expire_date = expire_date;
    }

    public void setExpire_time(LocalTime expire_time) {
        this.expire_time = expire_time;
    }

    public void setClaim_ip(String claim_ip) {
        this.claim_ip = claim_ip;
    }

    public void setClaim_timestamp(LocalDateTime claim_timestamp) {
        this.claim_timestamp = claim_timestamp;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof PinDTO)) return false;
        final PinDTO other = (PinDTO) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$account = this.getAccount();
        final Object other$account = other.getAccount();
        if (this$account == null ? other$account != null : !this$account.equals(other$account)) return false;
        final Object this$pin = this.getPin();
        final Object other$pin = other.getPin();
        if (this$pin == null ? other$pin != null : !this$pin.equals(other$pin)) return false;
        final Object this$claim_user = this.getClaim_user();
        final Object other$claim_user = other.getClaim_user();
        if (this$claim_user == null ? other$claim_user != null : !this$claim_user.equals(other$claim_user))
            return false;
        final Object this$expire_date = this.getExpire_date();
        final Object other$expire_date = other.getExpire_date();
        if (this$expire_date == null ? other$expire_date != null : !this$expire_date.equals(other$expire_date))
            return false;
        final Object this$expire_time = this.getExpire_time();
        final Object other$expire_time = other.getExpire_time();
        if (this$expire_time == null ? other$expire_time != null : !this$expire_time.equals(other$expire_time))
            return false;
        final Object this$claim_ip = this.getClaim_ip();
        final Object other$claim_ip = other.getClaim_ip();
        if (this$claim_ip == null ? other$claim_ip != null : !this$claim_ip.equals(other$claim_ip)) return false;
        final Object this$claim_timestamp = this.getClaim_timestamp();
        final Object other$claim_timestamp = other.getClaim_timestamp();
        if (this$claim_timestamp == null ? other$claim_timestamp != null : !this$claim_timestamp.equals(other$claim_timestamp))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $account = this.getAccount();
        result = result * PRIME + ($account == null ? 43 : $account.hashCode());
        final Object $pin = this.getPin();
        result = result * PRIME + ($pin == null ? 43 : $pin.hashCode());
        final Object $claim_user = this.getClaim_user();
        result = result * PRIME + ($claim_user == null ? 43 : $claim_user.hashCode());
        final Object $expire_date = this.getExpire_date();
        result = result * PRIME + ($expire_date == null ? 43 : $expire_date.hashCode());
        final Object $expire_time = this.getExpire_time();
        result = result * PRIME + ($expire_time == null ? 43 : $expire_time.hashCode());
        final Object $claim_ip = this.getClaim_ip();
        result = result * PRIME + ($claim_ip == null ? 43 : $claim_ip.hashCode());
        final Object $claim_timestamp = this.getClaim_timestamp();
        result = result * PRIME + ($claim_timestamp == null ? 43 : $claim_timestamp.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof PinDTO;
    }

    public String toString() {
        return "PinDTO(id=" + this.getId() + ", account=" + this.getAccount() + ", pin=" + this.getPin() + ", claim_user=" + this.getClaim_user() + ", expire_date=" + this.getExpire_date() + ", expire_time=" + this.getExpire_time() + ", claim_ip=" + this.getClaim_ip() + ", claim_timestamp=" + this.getClaim_timestamp() + ")";
    }
}
