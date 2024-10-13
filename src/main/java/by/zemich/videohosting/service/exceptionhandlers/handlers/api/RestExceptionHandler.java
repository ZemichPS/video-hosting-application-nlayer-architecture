package by.zemich.videohosting.service.exceptionhandlers.handlers.api;

import by.zemich.videohosting.service.exceptionhandlers.ExceptionHandlerHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.function.ServerResponse;

public interface RestExceptionHandler<T extends Throwable> {

    ServerResponse handleException(Throwable exception);

    Class<T> getExceptionType();

    @Autowired
    default void regMe(ExceptionHandlerHolder handlerHolder){
        handlerHolder.register(this);
    }

}
