package by.zemich.videohosting.service;

import by.zemich.videohosting.dao.entities.User;
import by.zemich.videohosting.dao.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Mono<User> findById(UUID userUuid) {
        return userRepository.findById(userUuid);
    }

    public Mono<User> save(User user) {
        return userRepository.save(user);
    }

}
