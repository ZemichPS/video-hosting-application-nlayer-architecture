package by.zemich.videohosting.service;

import by.zemich.videohosting.dao.entities.Channel;
import by.zemich.videohosting.dao.entities.User;
import by.zemich.videohosting.models.dto.request.UserData;
import by.zemich.videohosting.models.dto.response.ChannelShortRepresentation;
import by.zemich.videohosting.models.dto.response.UseRepresentation;
import by.zemich.videohosting.models.exceptions.ChannelNotFountException;
import by.zemich.videohosting.models.exceptions.UserNotFountException;
import by.zemich.videohosting.service.mappers.ChannelMapper;
import by.zemich.videohosting.service.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceFacade {
    private final UserCrudService userCrudService;
    private final ChannelService channelService;

    public Mono<User> subscribeUserToChannel(UUID userId, UUID channelId) {

        Mono<User> userMono = userCrudService.findById(userId);

        Mono<Channel> channelMono = channelService.findById(channelId)
                .switchIfEmpty(Mono.error(
                        new UserNotFountException("User with id = %s is nowhere to be found".formatted(userId))));

        return channelMono.zipWith(userMono, (channel, user) -> {
                    user.subscribe(channel);
                    return user;
                })
                .flatMap(userCrudService::save)
                .doOnSuccess(savedUser -> log.info("User was subscribed to channel", savedUser))
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to subscribeUserToChannel user to channel to user", e)));
    }

    public Mono<Void> unsubscribeFromChannel(UUID userId, UUID channelId) {

        Mono<User> userMono = userCrudService.findById(userId);
        Mono<Channel> channelMono = channelService.findById(channelId)
                .switchIfEmpty(Mono.error(
                        new ChannelNotFountException("Channel with id = %s is nowhere to be found".formatted(channelId))));

        return channelMono.zipWith(userMono, (channel, user) -> {
                    user.unsubscribe(channel);
                    return user;
                })
                .flatMap(userCrudService::save)
                .doOnSuccess(aVoid -> log.info("User was unsubscribed from the channel"))
                .then()
                .onErrorResume(e -> {
                    log.error("Failed to unsubscribe user from channel", e);
                    return Mono.error(new RuntimeException("Failed to unsubscribe user from channel", e));
                });
    }
    public Mono<UseRepresentation> create(UserData userData) {
        User user = UserMapper.INSTANCE.userDataToNewUser(userData);
        return userCrudService.save(user)
                .flatMap(savedUser -> Mono.just(UserMapper.INSTANCE.userToUserRepresentation(savedUser)));
    }

    public Mono<UseRepresentation> findById(UUID userId) {
        return userCrudService.findById(userId)
                .flatMap(user -> Mono.just(UserMapper.INSTANCE.userToUserRepresentation(user)));
    }

    public Mono<UseRepresentation> updateById(UserData userData, UUID userId) {
        return userCrudService.findById(userId)
                .flatMap(foundedUser -> {
                    foundedUser = UserMapper.INSTANCE.userDataToExistingUser(userData, foundedUser);
                    return userCrudService.save(foundedUser);
                })
                .flatMap(savedUser -> Mono.just(UserMapper.INSTANCE.userToUserRepresentation(savedUser)));
    }

    public Mono<UseRepresentation> updateById(Map<String, Object> updates, UUID userId) {
        return userCrudService.findById(userId)
                .flatMap(foundedUser -> {
                    if (updates.containsKey("username")) {
                        foundedUser.setName((String) updates.get("name"));
                    }
                    if (updates.containsKey("email")) {
                        foundedUser.setUsername((String) updates.get("username"));
                    }
                    if (updates.containsKey("name")) {
                        foundedUser.setEmail((String) updates.get("email"));
                    }
                    return userCrudService.save(foundedUser);
                })
                .flatMap(savedUser -> Mono.just(UserMapper.INSTANCE.userToUserRepresentation(savedUser)));
    }

    public Mono<Void> deleteById(UUID userId) {
        return userCrudService.deleteById(userId);
    }

    public Flux<ChannelShortRepresentation> findAllChannelsById(UUID userId) {
        return userCrudService.findById(userId)
                .flatMapMany(foundedUser -> {
                    Set<ChannelShortRepresentation> shortRepresentations = foundedUser.getSubscriptions().stream()
                            .map(ChannelMapper.INSTANCE::channelToShortRepresentation)
                            .collect(Collectors.toSet());
                    return Flux.fromIterable(shortRepresentations);
                });
    }


}
