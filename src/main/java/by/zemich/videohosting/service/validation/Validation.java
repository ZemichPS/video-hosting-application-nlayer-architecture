package by.zemich.videohosting.service.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Validation {
    private final Validator validator;

    public  <T> void validate(T validated) {
        Set<ConstraintViolation<T>> violations = validator.validate(validated);
        if (!violations.isEmpty()) {
            String message = "Validation failed. " + violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(" \n "));
            throw new ValidationException(message);
        }
    }
}
