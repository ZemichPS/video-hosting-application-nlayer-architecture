package by.zemich.videohosting.dao.repositories;

import by.zemich.videohosting.dao.entities.Category;
import by.zemich.videohosting.dao.entities.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, UUID> {

}
