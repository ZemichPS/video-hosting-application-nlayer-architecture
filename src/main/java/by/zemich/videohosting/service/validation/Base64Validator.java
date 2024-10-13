package by.zemich.videohosting.service.validation;

import by.zemich.videohosting.core.annotations.ValidBase64;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Base64;

public class Base64Validator implements ConstraintValidator<ValidBase64, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null || value.isEmpty()) {
            return true; // Пустое значение считается допустимым
        }
        try {
            // Попытка декодировать строку в ValidBase64
            Base64.getDecoder().decode(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
