package cgm.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = DateAfterDateValidator.class)
public @interface DateAfterDate {
    String message() default "Return date cannot be before departure date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
