package edu.ucmo.fightingmongeese.pinapp.models;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorDTO {

    private List<FieldErrorDTO> fieldErrors = new ArrayList<>();

    public ValidationErrorDTO() {}

    public void addFieldError(String path, String message) {
        FieldErrorDTO error = new FieldErrorDTO(path, message);
        fieldErrors.add(error);
    }

    public List<FieldErrorDTO> getFieldErrors() {
        return this.fieldErrors;
    }

    public void setFieldErrors(List<FieldErrorDTO> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof ValidationErrorDTO)) return false;
        final ValidationErrorDTO other = (ValidationErrorDTO) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$fieldErrors = this.getFieldErrors();
        final Object other$fieldErrors = other.getFieldErrors();
        if (this$fieldErrors == null ? other$fieldErrors != null : !this$fieldErrors.equals(other$fieldErrors))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $fieldErrors = this.getFieldErrors();
        result = result * PRIME + ($fieldErrors == null ? 43 : $fieldErrors.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ValidationErrorDTO;
    }

    public String toString() {
        return "ValidationErrorDTO(fieldErrors=" + this.getFieldErrors() + ")";
    }
}
