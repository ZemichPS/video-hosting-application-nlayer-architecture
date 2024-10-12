package by.zemich.videohosting.service.mappers;

import by.zemich.videohosting.dao.entities.User;
import by.zemich.videohosting.core.models.dto.request.UserData;
import by.zemich.videohosting.core.models.dto.response.UseRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User userDataToNewUser(UserData userData);

    void userDataToExistingUser(UserData userData, @MappingTarget User user);

    UseRepresentation toUserRepresentation(User user);

}
