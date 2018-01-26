package edu.ucmo.fightingmongeese.pinapp.models;

import edu.ucmo.fightingmongeese.pinapp.validators.*;
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

    @NotNull(groups = Add.class,
            message = "Account must be provided in request")
    @Column(name = "account", nullable = false)
    @SingleActivePin(groups = Add.class)
    @AccountFormat(groups = Add.class)
    private String account;

    @NotNull(groups = {Claim.class, Cancel.class},
            message = "PIN must be provided in request")
    @CheckPinExists(groups = {Claim.class, Cancel.class})
    @ClaimBeforeExpiration(groups = Claim.class)
    @Unclaimed(groups = Claim.class)
    @Pattern(regexp = "\\p{N}+", message = "PIN must be numeric")
    @Column(name = "pin", nullable = false, length = 6, unique = true)
    private String pin;

    @Column(name = "create_ip", nullable = false)
    private String create_ip;

    @CreateUserRequired(groups = Add.class)
    @Column(name = "create_user", nullable = false)
    private String create_user;


    @Column(name = "create_timestamp", updatable = false)
    private LocalDateTime create_timestamp;


    @Column(name = "expire_timestamp")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExpireTime(groups = Add.class)
    private LocalDateTime expire_timestamp;


    @Column(name = "claim_timestamp")
    private LocalDateTime claim_timestamp;

    @NotNull(groups = {Claim.class, Cancel.class},
            message = "Claim user is required")
    @Column(name = "claim_user")
    private String claim_user;

    @Column(name = "claim_ip")
    private String claim_ip;

    public Pin() {
    } //Empty Constructor for JPA

    // Constructor for start-up runner
    public Pin(String account, String create_ip, String create_user) {
        this.account = account;
        this.create_ip = create_ip;
        this.create_user = create_user;
    }

    // Constructor for tests
    public Pin(String account, String create_ip, String create_user, String claim_ip) {
        this.account = account;
        this.create_ip = create_ip;
        this.create_user = create_user;
        this.claim_ip = claim_ip;
    }

    public Pin(String account, String pin, String create_ip, String create_user, LocalDateTime create_timestamp, LocalDateTime expire_timestamp) {
        this.account = account;
        this.pin = pin;
        this.create_ip = create_ip;
        this.create_user = create_user;
        this.create_timestamp = create_timestamp;
        this.expire_timestamp = expire_timestamp;
    }

    public Pin(String account, String create_user) {
        this.account = account;
        this.create_user = create_user;
    }

    public interface Add {
    }

    public interface Cancel {
    }

    public interface Claim {
    }
}
