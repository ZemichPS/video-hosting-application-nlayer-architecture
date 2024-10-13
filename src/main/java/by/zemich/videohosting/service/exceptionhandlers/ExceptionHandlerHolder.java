package by.zemich.videohosting.service.exceptionhandlers;

import by.zemich.videohosting.service.exceptionhandlers.handlers.api.RestExceptionHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.HashMap;
import java.util.Map;

@Component

public class ExceptionHandlerHolder {


    private final Map<Class<? extends Throwable>, RestExceptionHandler<?>> handlers = new HashMap<>();

    public void register(RestExceptionHandler<?> handler) {
        handlers.put(handler.getExceptionType(), handler);
    }

    public ServerResponse handle(Throwable exception) {
        RestExceptionHandler<?> handler = handlers.entrySet().stream()
                .filter(entry -> entry.getKey().isAssignableFrom(exception.getClass()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(handlers.get(Exception.class));
        return handler.handleException(exception);
    }
}
