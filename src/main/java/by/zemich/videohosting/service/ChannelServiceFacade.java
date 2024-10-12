package by.zemich.videohosting.service;

import by.zemich.videohosting.core.exceptions.CategoryNotFoundException;
import by.zemich.videohosting.core.exceptions.ChannelNotFoundException;
import by.zemich.videohosting.core.exceptions.UserNotFoundException;
import by.zemich.videohosting.core.models.dto.request.ChannelData;
import by.zemich.videohosting.core.models.dto.response.ChannelFullRepresentation;
import by.zemich.videohosting.dao.entities.Category;
import by.zemich.videohosting.dao.entities.Channel;
import by.zemich.videohosting.dao.entities.User;
import by.zemich.videohosting.service.api.ChannelCrudService;
import by.zemich.videohosting.service.api.UserCrudService;
import by.zemich.videohosting.service.mappers.ChannelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChannelServiceFacade {
    private final ChannelCrudService channelCrudService;
    private final CategoryCrudService categoryCrudService;
    private final UserCrudService userCrudService;

    public ChannelFullRepresentation create(ChannelData channelData) {
        UUID authorId = channelData.getAuthorId();
        UUID categoryId = channelData.getCategoryId();

        User author = userCrudService.findById(authorId)
                .orElseThrow(() -> new UserNotFoundException("User with id %s is nowhere to be found".formatted(authorId)));
        Category category = categoryCrudService.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id %s is nowhere to be found".formatted(categoryId)));

        Channel channel = ChannelMapper.INSTANCE.channelDataToNewChannel(channelData);
        channel.addAuthor(author);
        channel.assignCategory(category);
        Channel savedChannel = channelCrudService.save(channel);
        return ChannelMapper.INSTANCE.toFullRepresentation(savedChannel);

    }

    public void deleteById(UUID channelUuid) {
        if(!channelCrudService.existsById(channelUuid)) throw new ChannelNotFoundException("Channel with id %s is nowhere to be found".formatted(channelUuid));
        channelCrudService.deleteById(channelUuid);
    }
}
