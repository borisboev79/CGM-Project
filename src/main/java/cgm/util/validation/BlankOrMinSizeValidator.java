package cgm.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BlankOrMinSizeValidator implements ConstraintValidator<BlankOrMinSize, String> {
    @Override
    public void initialize(BlankOrMinSize constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        return (value == null || value.trim().length() == 0 || value.length() > 3);
    }
}
