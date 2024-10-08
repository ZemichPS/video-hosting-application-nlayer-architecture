package by.zemich.videohosting.models.exceptions;

public class UserNotFountException extends RuntimeException {
    public UserNotFountException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFountException(String message) {}
}
