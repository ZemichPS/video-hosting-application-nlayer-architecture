package by.zemich.videohosting.service.exceptionhandlers.handlers;

import by.zemich.videohosting.core.exceptions.EntityNotFoundAbstractException;
import by.zemich.videohosting.core.models.dto.response.ErrorResponse;
import by.zemich.videohosting.service.exceptionhandlers.handlers.api.RestExceptionHandler;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerResponse;

@Component
public class RequaredEntityNotFoundRestExceptionHandler implements RestExceptionHandler<EntityNotFoundAbstractException> {
    @Override
    public ServerResponse handleException(Throwable exception) {
        ErrorResponse responseBody = new ErrorResponse(404, exception.getMessage());
        return ServerResponse.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
    }

    @Override
    public Class<EntityNotFoundAbstractException> getExceptionType() {
        return EntityNotFoundAbstractException.class;
    }

}
