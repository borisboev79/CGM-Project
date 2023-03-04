package cgm.util.validation;

import cgm.model.dto.GroupAddDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateAfterDateValidator implements ConstraintValidator<DateAfterDate, GroupAddDto> {

    @Override
    public void initialize(DateAfterDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(GroupAddDto value, ConstraintValidatorContext context) {
        if(value.getStartDate() == null || value.getEndDate() == null){
            return false;
        }
        return value.getStartDate().isBefore(value.getEndDate());
    }

}
