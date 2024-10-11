package by.zemich.videohosting.service;

import by.zemich.videohosting.dao.entities.User;
import by.zemich.videohosting.dao.repositories.UserRepository;
import by.zemich.videohosting.models.exceptions.UserNotFountException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserCrudService {
    private final UserRepository userRepository;

    public Mono<User> findById(UUID userId) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(
                        new UserNotFountException("User with id = %s is nowhere to be found".formatted(userId)))
                );
    }

    public Mono<User> save(User user) {
        return userRepository.save(user);
    }

    public Mono<Void> deleteById(UUID userId) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new UserNotFountException("User with id %s is nowhere to be found".formatted(userId))))
                .flatMap(foundedUser -> {
                    userRepository.deleteById(userId);
                    return Mono.empty();
                });
    }

}
