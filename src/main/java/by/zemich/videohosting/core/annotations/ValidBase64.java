package by.zemich.videohosting.core.annotations;

import by.zemich.videohosting.service.validation.Base64Validator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = Base64Validator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBase64 {

    String message() default "Invalid ValidBase64 format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
