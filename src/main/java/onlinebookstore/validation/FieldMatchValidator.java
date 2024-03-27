package onlinebookstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import onlinebookstore.dto.user.UserRegistrationRequestDto;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, UserRegistrationRequestDto> {
    @Override
    public boolean isValid(UserRegistrationRequestDto requestDto,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (requestDto.getPassword() == null) {
            return false;
        }
        return requestDto.getPassword().equals(requestDto.getRepeatPassword());
    }
}
