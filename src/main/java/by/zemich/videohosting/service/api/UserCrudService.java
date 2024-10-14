package by.zemich.videohosting.service.api;

import by.zemich.videohosting.dao.entities.User;

import java.util.UUID;

public interface UserCrudService extends CrudService<User, UUID> {
    boolean existsByUsername(String username);
}
