package edu.ucmo.fightingmongeese.pinapp.models;

import edu.ucmo.fightingmongeese.pinapp.validators.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import java.util.regex.Pattern;

/**
 * Class used to model a PIN object and associated state.
 * Spring handles translation between the Pin as JSON, a Java object,
 * and as a row in the SQL database.
 * <p>
 * All field validation is annotated here and performed before the
 * request arrives at the associated controller.
 * Field validation error messages are stored in the configuration file
 * located at src.main.resources.ValidationMessages.properties
 */
@Entity
@Table(name = "pins")
public class Pin {

    private static final int SIZE_OF_RAW_PIN = 6;
    private static final int SIZE_OF_CHECKED_PIN = SIZE_OF_RAW_PIN + 1;
    public static final java.util.regex.Pattern pattern = Pattern.compile("\\p{N}{" + SIZE_OF_CHECKED_PIN + "}");


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer oid;

    @AccountValidators(groups = Add.class)
    @Column(name = "account", nullable = false)
    private String account;

    @PinValidators(groups = {Claim.class, Cancel.class})
    @Column(name = "pin", nullable = false, length = SIZE_OF_CHECKED_PIN, unique = true)
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
            message = "{edu.ucmo.fightingmongeese.defaultClaimUserRequiredMessage}")
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

    // another test constructor
    public Pin(String account, String pin, String create_ip, String create_user, LocalDateTime create_timestamp, LocalDateTime expire_timestamp) {
        this.account = account;
        this.pin = pin;
        this.create_ip = create_ip;
        this.create_user = create_user;
        this.create_timestamp = create_timestamp;
        this.expire_timestamp = expire_timestamp;
    }

    // another one
    public Pin(String account, String create_user) {
        this.account = account;
        this.create_user = create_user;
    }

    public static boolean correctPinFormat(String pinString) {
        return pattern.matcher(pinString).matches();
    }

    public static String getPinWithChecksum(String pinString) {

        int checkSumDigit = getLuhnSum(pinString) * 9 % 10;
        return pinString + checkSumDigit;
    }

    private static int getLuhnSum(String pinString) {
        int[] multiplier = {SIZE_OF_RAW_PIN % 2 == 0 ? 2 : 1}; // Allows for easier refactoring to larger PINs
        return pinString.chars()
                .map(i -> i - '0')  // convert ASCII value to int
                .map(n -> n * (multiplier[0] = multiplier[0] == 1 ? 2 : 1)) // alternate multiplier
                .map(n -> n > 9 ? n - 9 : n)    // adds digits of two digit numbers
                .sum();
    }

    public static boolean isValidLuhn(String pinString) {
        return getLuhnSum(pinString) % 10 == 0;

    }

    public Integer getOid() {
        return this.oid;
    }

    public String getAccount() {
        return this.account;
    }

    public String getPin() {
        return this.pin;
    }

    public String getCreate_ip() {
        return this.create_ip;
    }

    public String getCreate_user() {
        return this.create_user;
    }

    public LocalDateTime getCreate_timestamp() {
        return this.create_timestamp;
    }

    public LocalDateTime getExpire_timestamp() {
        return this.expire_timestamp;
    }

    public LocalDateTime getClaim_timestamp() {
        return this.claim_timestamp;
    }

    public String getClaim_user() {
        return this.claim_user;
    }

    public String getClaim_ip() {
        return this.claim_ip;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setCreate_ip(String create_ip) {
        this.create_ip = create_ip;
    }

    public void setCreate_user(String create_user) {
        this.create_user = create_user;
    }

    public void setCreate_timestamp(LocalDateTime create_timestamp) {
        this.create_timestamp = create_timestamp;
    }

    public void setExpire_timestamp(LocalDateTime expire_timestamp) {
        this.expire_timestamp = expire_timestamp;
    }

    public void setClaim_timestamp(LocalDateTime claim_timestamp) {
        this.claim_timestamp = claim_timestamp;
    }

    public void setClaim_user(String claim_user) {
        this.claim_user = claim_user;
    }

    public void setClaim_ip(String claim_ip) {
        this.claim_ip = claim_ip;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Pin)) return false;
        final Pin other = (Pin) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$oid = this.getOid();
        final Object other$oid = other.getOid();
        if (this$oid == null ? other$oid != null : !this$oid.equals(other$oid)) return false;
        final Object this$account = this.getAccount();
        final Object other$account = other.getAccount();
        if (this$account == null ? other$account != null : !this$account.equals(other$account)) return false;
        final Object this$pin = this.getPin();
        final Object other$pin = other.getPin();
        if (this$pin == null ? other$pin != null : !this$pin.equals(other$pin)) return false;
        final Object this$create_ip = this.getCreate_ip();
        final Object other$create_ip = other.getCreate_ip();
        if (this$create_ip == null ? other$create_ip != null : !this$create_ip.equals(other$create_ip)) return false;
        final Object this$create_user = this.getCreate_user();
        final Object other$create_user = other.getCreate_user();
        if (this$create_user == null ? other$create_user != null : !this$create_user.equals(other$create_user))
            return false;
        final Object this$create_timestamp = this.getCreate_timestamp();
        final Object other$create_timestamp = other.getCreate_timestamp();
        if (this$create_timestamp == null ? other$create_timestamp != null : !this$create_timestamp.equals(other$create_timestamp))
            return false;
        final Object this$expire_timestamp = this.getExpire_timestamp();
        final Object other$expire_timestamp = other.getExpire_timestamp();
        if (this$expire_timestamp == null ? other$expire_timestamp != null : !this$expire_timestamp.equals(other$expire_timestamp))
            return false;
        final Object this$claim_timestamp = this.getClaim_timestamp();
        final Object other$claim_timestamp = other.getClaim_timestamp();
        if (this$claim_timestamp == null ? other$claim_timestamp != null : !this$claim_timestamp.equals(other$claim_timestamp))
            return false;
        final Object this$claim_user = this.getClaim_user();
        final Object other$claim_user = other.getClaim_user();
        if (this$claim_user == null ? other$claim_user != null : !this$claim_user.equals(other$claim_user))
            return false;
        final Object this$claim_ip = this.getClaim_ip();
        final Object other$claim_ip = other.getClaim_ip();
        if (this$claim_ip == null ? other$claim_ip != null : !this$claim_ip.equals(other$claim_ip)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $oid = this.getOid();
        result = result * PRIME + ($oid == null ? 43 : $oid.hashCode());
        final Object $account = this.getAccount();
        result = result * PRIME + ($account == null ? 43 : $account.hashCode());
        final Object $pin = this.getPin();
        result = result * PRIME + ($pin == null ? 43 : $pin.hashCode());
        final Object $create_ip = this.getCreate_ip();
        result = result * PRIME + ($create_ip == null ? 43 : $create_ip.hashCode());
        final Object $create_user = this.getCreate_user();
        result = result * PRIME + ($create_user == null ? 43 : $create_user.hashCode());
        final Object $create_timestamp = this.getCreate_timestamp();
        result = result * PRIME + ($create_timestamp == null ? 43 : $create_timestamp.hashCode());
        final Object $expire_timestamp = this.getExpire_timestamp();
        result = result * PRIME + ($expire_timestamp == null ? 43 : $expire_timestamp.hashCode());
        final Object $claim_timestamp = this.getClaim_timestamp();
        result = result * PRIME + ($claim_timestamp == null ? 43 : $claim_timestamp.hashCode());
        final Object $claim_user = this.getClaim_user();
        result = result * PRIME + ($claim_user == null ? 43 : $claim_user.hashCode());
        final Object $claim_ip = this.getClaim_ip();
        result = result * PRIME + ($claim_ip == null ? 43 : $claim_ip.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Pin;
    }

    public String toString() {
        return "Pin(oid=" + this.getOid() + ", account=" + this.getAccount() + ", pin=" + this.getPin() + ", create_ip=" + this.getCreate_ip() + ", create_user=" + this.getCreate_user() + ", create_timestamp=" + this.getCreate_timestamp() + ", expire_timestamp=" + this.getExpire_timestamp() + ", claim_timestamp=" + this.getClaim_timestamp() + ", claim_user=" + this.getClaim_user() + ", claim_ip=" + this.getClaim_ip() + ")";
    }

    public interface Add {
    }

    public interface Cancel {
    }

    public interface Claim {
    }
}
