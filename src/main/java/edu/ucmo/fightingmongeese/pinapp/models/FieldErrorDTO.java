package edu.ucmo.fightingmongeese.pinapp.models;

public class FieldErrorDTO {

    private String field;

    private String message;

    public FieldErrorDTO(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return this.field;
    }

    public String getMessage() {
        return this.message;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof FieldErrorDTO)) return false;
        final FieldErrorDTO other = (FieldErrorDTO) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$field = this.getField();
        final Object other$field = other.getField();
        if (this$field == null ? other$field != null : !this$field.equals(other$field)) return false;
        final Object this$message = this.getMessage();
        final Object other$message = other.getMessage();
        if (this$message == null ? other$message != null : !this$message.equals(other$message)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $field = this.getField();
        result = result * PRIME + ($field == null ? 43 : $field.hashCode());
        final Object $message = this.getMessage();
        result = result * PRIME + ($message == null ? 43 : $message.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof FieldErrorDTO;
    }

    public String toString() {
        return "FieldErrorDTO(field=" + this.getField() + ", message=" + this.getMessage() + ")";
    }
}
