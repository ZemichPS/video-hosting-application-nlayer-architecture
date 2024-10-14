package by.zemich.videohosting.core.exceptions;

import java.util.UUID;

public class UserWithSuchUsernameAlreadyExists extends RuntimeException {

    public UserWithSuchUsernameAlreadyExists(String username) {
        super(String.format("User %s already exists", username));
    }
}
