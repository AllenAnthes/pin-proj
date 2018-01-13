package edu.ucmo.fightingmongeese.pinapp.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "pins")
public class Pin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer oid;
//
//    @NotEmpty
//    @Column(name = "oid", nullable = false)
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long oid;

    @NotEmpty
    @Column(name = "account", nullable = false)
    private String account;

    @NotEmpty
    @Column(name = "pin", nullable = false, length = 6, unique = true)
    private String pin;

    @NotEmpty
    @Column(name = "create_ip", nullable = false)
    private String create_ip;

    @NotEmpty
    @Column(name = "create_user", nullable = false)
    private String create_user;

//    @NotEmpty
    @Column(name = "create_timestamp",
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", updatable = false)
    @CreationTimestamp
    private Timestamp create_timestamp;
//
//
//    @Column(name = "expire_timestamp",
//            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
////    @Temporal(TemporalType.TIMESTAMP)
//    private Timestamp expire_timestamp;

    @Column(name = "expire_timestamp",
            columnDefinition = "TIMESTAMP NULL DEFAULT NULL")
    private Timestamp expire_timestamp;


    @Column(name = "claim_timestamp",
            columnDefinition = "TIMESTAMP NULL DEFAULT NULL")
    private Timestamp claim_timestamp;


    @Column(name = "claim_user")
    private String claim_user;

    @Column(name = "claim_ip")
    private String claim_ip;

    public Pin() {}

    public Pin(String account, String pin, String create_ip, String create_user, Timestamp create_timestamp, String claim_user, String claim_ip) {
        this.account = account;
        this.pin = pin;
        this.create_ip = create_ip;
        this.create_user = create_user;
        this.create_timestamp = create_timestamp;
        this.claim_user = claim_user;
        this.claim_ip = claim_ip;
    }

    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getCreate_ip() {
        return create_ip;
    }

    public void setCreate_ip(String create_ip) {
        this.create_ip = create_ip;
    }

    public String getCreate_user() {
        return create_user;
    }

    public void setCreate_user(String create_user) {
        this.create_user = create_user;
    }

    public Timestamp getCreate_timestamp() {
        return create_timestamp;
    }

    public void setCreate_timestamp(Timestamp create_timestamp) {
        this.create_timestamp = create_timestamp;
    }

    public String getClaim_user() {
        return claim_user;
    }

    public void setClaim_user(String claim_user) {
        this.claim_user = claim_user;
    }

    public String getClaim_ip() {
        return claim_ip;
    }

    public void setClaim_ip(String claim_ip) {
        this.claim_ip = claim_ip;
    }

    public Timestamp getClaim_timestamp() {
        return claim_timestamp;
    }

    public void setClaim_timestamp(Timestamp claim_timestamp) {
        this.claim_timestamp = claim_timestamp;
    }

    public Timestamp getExpire_timestamp() {
        return expire_timestamp;
    }

    public void setExpire_timestamp(Timestamp expire_timestamp) {
        this.expire_timestamp = expire_timestamp;
    }
}
