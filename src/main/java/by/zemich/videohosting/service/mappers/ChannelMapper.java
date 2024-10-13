package by.zemich.videohosting.service.mappers;

import by.zemich.videohosting.core.models.dto.request.ChannelData;
import by.zemich.videohosting.core.models.dto.response.*;
import by.zemich.videohosting.dao.entities.Category;
import by.zemich.videohosting.dao.entities.Channel;
import by.zemich.videohosting.dao.entities.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Base64;
import java.util.Set;
import java.util.UUID;

@Mapper
public interface ChannelMapper {

    ChannelMapper INSTANCE = Mappers.getMapper(ChannelMapper.class);

    Channel channelDataToNewChannel(ChannelData channelData);

    void channelDataToExistingChannel(ChannelData channelData, @MappingTarget Channel channel);

    ChannelShortRepresentation toShortRepresentation(Channel channel);

    @Mappings({
            @Mapping(source = "category", target = "category", qualifiedByName = "categoryToShortRepresentation"),
            @Mapping(source = "subscribers", target = "subscribersCount", qualifiedByName = "subscriberCollectionToCount")
    })
    ChannelPageContent channelToPageContent(Channel channel);

    @Mappings({
            @Mapping(source = "author", target = "authorId", qualifiedByName = "userToAuthorId"),
            @Mapping(source = "createdAt", target = "creationDate"),
            @Mapping(source = "category", target = "category", qualifiedByName = "toCategoryShortRepresentation")
    })
    ChannelFullRepresentation toFullRepresentation(Channel channel);


    @Named("categoryToShortRepresentation")
    default CategoryShortRepresentation categoryToShortRepresentation(Category category) {
        if (category == null) return null;
        return new CategoryShortRepresentation(category.getId(), category.getName());
    }

    @Named("userToAuthorId")
    default AuthorId userToAuthorId(User user) {
        return user != null ? new AuthorId(user.getId()) : null;
    }

    @Named("subscriberCollectionToCount")
    default long getSubscriberCount(Set<User> users) {
        return users != null ? users.size() : null;
    }

    @Named("toCategoryShortRepresentation")
    default CategoryShortRepresentation toCategoryShortRepresentation(Category category) {
        UUID categoryId = category.getId();
        String categoryName = category.getName();
        return new CategoryShortRepresentation(categoryId, categoryName);
    }

}
