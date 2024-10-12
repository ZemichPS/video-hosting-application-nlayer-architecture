package by.zemich.videohosting.core.exceptions;

public class UserNotFoundException extends EntityNotFoundAbstractException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
