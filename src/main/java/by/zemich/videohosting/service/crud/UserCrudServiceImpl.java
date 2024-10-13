package by.zemich.videohosting.service.crud;

import by.zemich.videohosting.dao.entities.User;
import by.zemich.videohosting.dao.repositories.UserRepository;
import by.zemich.videohosting.service.api.UserCrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserCrudServiceImpl implements UserCrudService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Optional<User> findById(UUID userId) {
        return userRepository.findById(userId);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void deleteById(UUID userId) {
        userRepository.deleteById(userId);
    }

    public boolean existsById(UUID userId) {
        return userRepository.existsById(userId);
    }

}
