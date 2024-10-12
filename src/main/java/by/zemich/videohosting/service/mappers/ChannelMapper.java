package by.zemich.videohosting.service.mappers;

import by.zemich.videohosting.core.models.dto.request.ChannelData;
import by.zemich.videohosting.core.models.dto.response.AuthorId;
import by.zemich.videohosting.core.models.dto.response.CategoryShortRepresentation;
import by.zemich.videohosting.core.models.dto.response.ChannelFullRepresentation;
import by.zemich.videohosting.core.models.dto.response.ChannelShortRepresentation;
import by.zemich.videohosting.dao.entities.Category;
import by.zemich.videohosting.dao.entities.Channel;
import by.zemich.videohosting.dao.entities.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Base64;
import java.util.UUID;

@Mapper
public interface ChannelMapper {
    ChannelMapper INSTANCE = Mappers.getMapper(ChannelMapper.class);


    Channel channelDataToNewChannel(ChannelData channelData);

    void channelDataToExistingChannel(ChannelData channelData, @MappingTarget Channel channel);

    ChannelShortRepresentation toShortRepresentation(Channel channel);

    @Mappings({
            @Mapping(source = "author", target = "authorId", qualifiedByName = "userToAuthorId"),
            @Mapping(source = "createdAt", target = "creationDate"),
            @Mapping(source = "category", target = "category", qualifiedByName = "toCategoryShortRepresentation")
    })
    ChannelFullRepresentation toFullRepresentation(Channel channel);

    @Named("userToAuthorId")
    default AuthorId userToAuthorId(User user) {
        return user != null ? new AuthorId(user.getId()) : null;
    }

    @Named("toCategoryShortRepresentation")
    default CategoryShortRepresentation toCategoryShortRepresentation(Category category) {
        UUID categoryId = category.getId();
        String categoryName = category.getName();
        return new CategoryShortRepresentation(categoryId, categoryName);
    }

}
