package by.zemich.videohosting.core.exceptions;

public abstract class EntityNotFoundAbstractException extends RuntimeException {
    public EntityNotFoundAbstractException(String message) {
        super(message);
    }
}
