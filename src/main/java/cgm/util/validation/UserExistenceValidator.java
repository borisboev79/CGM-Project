package cgm.util.validation;


import cgm.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UserExistenceValidator implements ConstraintValidator<ValidateUserExistence, String> {

    private final UserRepository userRepository;

    @Autowired
    public UserExistenceValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void initialize(ValidateUserExistence constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        return this.userRepository.findByUsername(username).isEmpty();
    }
}
