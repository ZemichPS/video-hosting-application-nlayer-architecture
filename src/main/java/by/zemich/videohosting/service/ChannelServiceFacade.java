package by.zemich.videohosting.service;

import by.zemich.videohosting.core.exceptions.CategoryNotFoundException;
import by.zemich.videohosting.core.exceptions.ChannelNotFoundException;
import by.zemich.videohosting.core.exceptions.UserNotFoundException;
import by.zemich.videohosting.core.models.dto.request.ChannelData;
import by.zemich.videohosting.core.models.dto.response.ChannelFullRepresentation;
import by.zemich.videohosting.core.models.dto.response.ChannelPageContent;
import by.zemich.videohosting.core.models.dto.response.SubscriberShortRepresentation;
import by.zemich.videohosting.dao.entities.Category;
import by.zemich.videohosting.dao.entities.Channel;
import by.zemich.videohosting.dao.entities.User;
import by.zemich.videohosting.service.api.ChannelCrudService;
import by.zemich.videohosting.service.api.UserCrudService;
import by.zemich.videohosting.service.crud.CategoryCrudService;
import by.zemich.videohosting.service.mappers.ChannelMapper;
import by.zemich.videohosting.service.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

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
        channel.setAuthor(author);
        channel.assignCategory(category);
        Channel savedChannel = channelCrudService.save(channel);
        return ChannelMapper.INSTANCE.toFullRepresentation(savedChannel);

    }

    public void deleteById(UUID channelUuid) {
        if (!channelCrudService.existsById(channelUuid))
            throw new ChannelNotFoundException("Channel with id %s is nowhere to be found".formatted(channelUuid));
        channelCrudService.deleteById(channelUuid);
    }

    public ChannelFullRepresentation findById(UUID channelId) {
        return channelCrudService.findById(channelId)
                .map(ChannelMapper.INSTANCE::toFullRepresentation)
                .orElseThrow(() -> new CategoryNotFoundException("Channel with id %s is nowhere to be found".formatted(channelId)));
    }

    public ChannelFullRepresentation update(ChannelData channelData, UUID channelId) {
        Channel updatingChannel = channelCrudService.findById(channelId)
                .orElseThrow(() -> new CategoryNotFoundException("Channel with id %s is nowhere to be found".formatted(channelId)));
        ChannelMapper.INSTANCE.channelDataToExistingChannel(channelData, updatingChannel);

        UUID newAuthorId = channelData.getAuthorId();
        UUID newCategoryId = channelData.getCategoryId();

        if (!updatingChannel.getAuthor().getId().equals(newAuthorId)) {
            updateAuthorOfChannel(newAuthorId, updatingChannel);
        }
        if (!updatingChannel.getCategory().getId().equals(newCategoryId)) {
            updateCategoryOfChannel(newCategoryId, updatingChannel);
        }
        Channel savedChannel = channelCrudService.save(updatingChannel);
        return ChannelMapper.INSTANCE.toFullRepresentation(savedChannel);
    }

    public ChannelFullRepresentation patch(Map<String, Object> updates, UUID channelId) {
        return channelCrudService.findById(channelId)
                .map(updatingChannel -> {
                    if (updates.containsKey("name")) {
                        updatingChannel.setName((String) updates.get("name"));
                    }
                    if (updates.containsKey("description")) {
                        updatingChannel.setDescription((String) updates.get("description"));
                    }
                    if (updates.containsKey("language")) {
                        updatingChannel.setName((String) updates.get("language"));
                    }
                    if (updates.containsKey("avatar")) {
                        updatingChannel.setName((String) updates.get("avatar"));
                    }

                    if (updates.containsKey("authorId")) {
                        UUID newAuthorId = (UUID) updates.get("authorId");
                        if (!newAuthorId.equals(updatingChannel.getAuthor().getId())) {
                            updateAuthorOfChannel(newAuthorId, updatingChannel);
                        }
                    }
                    if (updates.containsKey("categoryId")) {
                        UUID categoryId = (UUID) updates.get("categoryId");
                        if (!categoryId.equals(updatingChannel.getCategory().getId())) {
                            updateAuthorOfChannel(categoryId, updatingChannel);
                        }
                    }
                    return updatingChannel;
                })
                .map(channelCrudService::save)
                .map(ChannelMapper.INSTANCE::toFullRepresentation)
                .orElseThrow(() -> new CategoryNotFoundException("Channel with id %s is nowhere to be found".formatted(channelId)));
    }

    private void updateAuthorOfChannel(UUID newAuthorId, Channel updatingChannel) {
        userCrudService.findById(newAuthorId)
                .ifPresentOrElse(updatingChannel::setAuthor,
                        () -> {
                            throw new UserNotFoundException("User with id %s is nowhere to be found".formatted(newAuthorId));
                        }
                );
    }

    private void updateCategoryOfChannel(UUID newCategoryId, Channel updatingChannel) {
        categoryCrudService.findById(newCategoryId)
                .ifPresentOrElse(updatingChannel::assignCategory,
                        () -> {
                            throw new CategoryNotFoundException("Channel with id %s is nowhere to be found".formatted(newCategoryId));
                        }
                );
    }


    public List<SubscriberShortRepresentation> findAllSubscribers(UUID channelId) {
        return channelCrudService.findById(channelId)
                .map(channel -> channel.getSubscribers().stream()
                        .map(UserMapper.INSTANCE::userToSubscriberShortRepresentation)
                        .toList())
                .orElseThrow(() -> new CategoryNotFoundException("Channel with id %s is nowhere to be found".formatted(channelId)));
    }

    public Page<ChannelPageContent> getPage(Pageable pageable, Predicate<Channel> filterPredicate) {
        Page<Channel> page = channelCrudService.findAll(pageable);

        List<ChannelPageContent> content = page.getContent().stream()
                .filter(filterPredicate)
                .map(ChannelMapper.INSTANCE::channelToPageContent)
                .toList();

        return new PageImpl<>(content, pageable, page.getTotalElements());

    }
}
