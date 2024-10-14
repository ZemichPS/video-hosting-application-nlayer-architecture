package by.zemich.videohosting.service.exceptionhandlers.handlers;

import by.zemich.videohosting.core.exceptions.UserWithSuchUsernameAlreadyExists;
import by.zemich.videohosting.core.models.dto.response.ErrorResponse;
import by.zemich.videohosting.service.exceptionhandlers.handlers.api.RestExceptionHandler;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerResponse;

@Component
public class UserAlreadyExistsRestExceptionHandler implements RestExceptionHandler<UserWithSuchUsernameAlreadyExists> {
    @Override
    public ServerResponse handleException(Throwable exception) {
        ErrorResponse responseBody = new ErrorResponse(400, exception.getMessage());
        return ServerResponse.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);

    }

    @Override
    public Class<UserWithSuchUsernameAlreadyExists> getExceptionType() {
        return UserWithSuchUsernameAlreadyExists.class;
    }


}
