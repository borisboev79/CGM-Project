package cgm.util.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = BlankOrMinSizeValidator.class)
public @interface BlankOrMinSize {

    String message() default "Username should be at least 3 characters long";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
