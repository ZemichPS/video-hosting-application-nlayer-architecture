package by.zemich.videohosting.service.exceptionhandlers.handlers;

import by.zemich.videohosting.core.models.dto.response.ErrorResponse;
import by.zemich.videohosting.service.exceptionhandlers.handlers.api.RestExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerResponse;

@Component
public class DefaultRestExceptionHandler implements RestExceptionHandler<Exception> {
    @Override
    public ServerResponse handleException(Throwable exception) {
        ErrorResponse responseBody = new ErrorResponse(500, "Something went wrong...");
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
    }

    @Override
    public Class<Exception> getExceptionType() {
        return Exception.class;
    }


}
