package by.zemich.videohosting.service.exceptionhandlers;

import by.zemich.videohosting.service.exceptionhandlers.handlers.api.RestExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class ExceptionHandlerHolder {

    private final Map<Class<? extends Throwable>, RestExceptionHandler<?>> handlers = new HashMap<>();

    public void register(RestExceptionHandler<?> handler) {
        handlers.put(handler.getExceptionType(), handler);
    }

    public ServerResponse handle(Throwable exception, ServerRequest request) {
        log.error("Handle request error: {}, Request: {}", exception.getMessage(), request);
        RestExceptionHandler<?> handler = handlers.entrySet().stream()
                .filter(entry -> entry.getKey().isAssignableFrom(exception.getClass()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(handlers.get(Exception.class));

        return handler.handleException(exception);
    }
}
