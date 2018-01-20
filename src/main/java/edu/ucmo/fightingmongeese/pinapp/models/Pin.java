package edu.ucmo.fightingmongeese.pinapp.models;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pins")
public class Pin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer oid;

    @NotNull(
            groups = Add.class,
            message = "Account must be supplied to add new PIN")
    @Pattern(regexp = "\\p{Alnum}+", message = "Account must be alphanumeric")
    @Column(name = "account", nullable = false)
    private String account;

    @NotNull(
            groups = {Claim.class, Cancel.class},
            message = "PIN must be provided in request"
    )
    @Pattern(regexp = "\\p{N}+", message = "PIN must be numeric")
    @Column(name = "pin", nullable = false, length = 6, unique = true)
    private String pin;

    @Column(name = "create_ip", nullable = false)
    private String create_ip;

    @NotNull(
            groups = Add.class,
            message = "User must be provided in request"
    )
    @Column(name = "create_user", nullable = false)
    private String create_user;


    @Column(name = "create_timestamp", updatable = false)
    private LocalDateTime create_timestamp;


    @Column(name = "expire_timestamp")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expire_timestamp;


    @Column(name = "claim_timestamp")
    private LocalDateTime claim_timestamp;

    @NotNull(
            groups = {Claim.class, Cancel.class}
    )
    @Column(name = "claim_user")
    private String claim_user;

    @Column(name = "claimIp")
    private String claimIp;

    public Pin() {
    } //Empty Constructor for JPA

    // Constructor for start-up runner
    public Pin(String account, String create_ip, String create_user) {
        this.account = account;
        this.create_ip = create_ip;
        this.create_user = create_user;
    }

    public interface Add {
    }

    public interface Cancel {
    }

    public interface Claim {
    }
}
