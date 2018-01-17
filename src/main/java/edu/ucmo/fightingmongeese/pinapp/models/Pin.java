package edu.ucmo.fightingmongeese.pinapp.models;

import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;


import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pins")
public class Pin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer oid;

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


    @Column(name = "create_timestamp", updatable = false)
    private LocalDateTime create_timestamp;


    @Column(name = "expire_timestamp")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expire_timestamp;


    @Column(name = "claim_timestamp")
    private LocalDateTime claim_timestamp;

    @Column(name = "claim_user")
    private String claim_user;

    @Column(name = "claim_ip")
    private String claim_ip;

    public Pin() {} //Empty Constructor for JPA

    // Constructor for start-up runner
    public Pin(String account, String create_ip, String create_user) {
        this.account = account;
        this.create_ip = create_ip;
        this.create_user = create_user;
    }

}
