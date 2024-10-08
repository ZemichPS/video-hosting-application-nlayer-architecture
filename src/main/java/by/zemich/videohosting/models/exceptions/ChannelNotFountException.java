package by.zemich.videohosting.models.exceptions;

public class ChannelNotFountException extends RuntimeException {
    public ChannelNotFountException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChannelNotFountException(String message) {}
}
