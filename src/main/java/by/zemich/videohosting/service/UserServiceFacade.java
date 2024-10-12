package by.zemich.videohosting.service;

import by.zemich.videohosting.dao.entities.User;
import by.zemich.videohosting.core.models.dto.request.UserData;
import by.zemich.videohosting.core.models.dto.response.ChannelShortRepresentation;
import by.zemich.videohosting.core.models.dto.response.UseRepresentation;
import by.zemich.videohosting.core.exceptions.ChannelNotFoundException;
import by.zemich.videohosting.core.exceptions.UserNotFoundException;
import by.zemich.videohosting.service.api.ChannelCrudService;
import by.zemich.videohosting.service.api.UserCrudService;
import by.zemich.videohosting.service.mappers.ChannelMapper;
import by.zemich.videohosting.service.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceFacade {
    private final UserCrudService userCrudService;
    private final ChannelCrudService channelService;

    public UseRepresentation subscribeUserToChannel(UUID userId, UUID channelId) throws UserNotFoundException, ChannelNotFoundException {
        return userCrudService.findById(userId).map(
                user -> channelService.findById(channelId).map(channel -> {
                    user.subscribe(channel);
                    log.info("User was subscribed to channel", user);
                    return UserMapper.INSTANCE.toUserRepresentation(user);
                }).orElseThrow(() -> new ChannelNotFoundException("Channel with id: % is nowhere to be found".formatted(channelId)))
        ).orElseThrow(() -> new UserNotFoundException("User with id: %s is nowhere to be found".formatted(userId)));
    }

    public void unsubscribeFromChannel(UUID userId, UUID channelId) throws UserNotFoundException, ChannelNotFoundException {
        userCrudService.findById(userId).ifPresentOrElse(
                user ->
                        channelService.findById(channelId).ifPresentOrElse(
                                channel -> {
                                    user.unsubscribe(channel);
                                    log.info("User was unsubscribed to channel", user);
                                }, () -> new UserNotFoundException("Channel with id: % is nowhere to be found".formatted(channelId))
                        ), () -> new UserNotFoundException("User with id: %s is nowhere to be found".formatted(userId)));
    }

    public UseRepresentation create(UserData userData) {
        User mewUser = UserMapper.INSTANCE.userDataToNewUser(userData);
        User savedUser = userCrudService.save(mewUser);
        return UserMapper.INSTANCE.toUserRepresentation(savedUser);
    }

    public UseRepresentation findById(UUID userId) throws UserNotFoundException {
        User user = userCrudService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: %s is nowhere to be found".formatted(userId)));
        return UserMapper.INSTANCE.toUserRepresentation(user);
    }

    public UseRepresentation updateById(UserData userData, UUID userId) throws UserNotFoundException {
        return userCrudService.findById(userId).map(user ->
                        {
                            UserMapper.INSTANCE.userDataToExistingUser(userData, user);
                            return userCrudService.save(user);
                        }
                ).map(UserMapper.INSTANCE::toUserRepresentation)
                .orElseThrow(() -> new UserNotFoundException("User with id: %s is nowhere to be found".formatted(userId)));
    }

    public UseRepresentation updateById(Map<String, Object> updates, UUID userId) {
        return userCrudService.findById(userId)
                .map(foundedUser -> {
                    if (updates.containsKey("username")) {
                        foundedUser.setUsername((String) updates.get("username"));
                    }
                    if (updates.containsKey("email")) {
                        foundedUser.setEmail((String) updates.get("email"));
                    }
                    if (updates.containsKey("name")) {
                        foundedUser.setName((String) updates.get("name"));
                    }
                    return userCrudService.save(foundedUser);
                })
                .map(UserMapper.INSTANCE::toUserRepresentation)
                .orElseThrow(() -> new UserNotFoundException("User with id: %s is nowhere to be found".formatted(userId)));
    }

    public void deleteById(UUID userId) throws UserNotFoundException {
        if (!userCrudService.existsById(userId))
            throw new UserNotFoundException("User with id: %s is nowhere to be found".formatted(userId));
        userCrudService.deleteById(userId);
    }

    public List<ChannelShortRepresentation> findAllChannelsById(UUID userId) {
        return userCrudService.findById(userId)
                .map(foundedUser -> foundedUser.getSubscriptions().stream()
                        .map(ChannelMapper.INSTANCE::toShortRepresentation)
                        .collect(Collectors.toList())).orElseThrow(() -> new UserNotFoundException("User with id: % is nowhere to be found".formatted(userId)));
    }


}
