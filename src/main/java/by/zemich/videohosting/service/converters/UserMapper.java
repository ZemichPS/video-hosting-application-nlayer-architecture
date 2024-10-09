package by.zemich.videohosting.service.converters;

import by.zemich.videohosting.dao.entities.User;
import by.zemich.videohosting.models.dto.request.UserCreate;
import by.zemich.videohosting.models.dto.response.UserCreated;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User userCreateToUser(UserCreate userCreate);

    UserCreated userToUserCreated(User user);

}
