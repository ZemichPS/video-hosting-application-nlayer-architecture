package by.zemich.videohosting.service;

import by.zemich.videohosting.dao.entities.Channel;
import by.zemich.videohosting.dao.entities.User;
import by.zemich.videohosting.models.dto.request.UserCreate;
import by.zemich.videohosting.models.dto.response.UserCreated;
import by.zemich.videohosting.models.exceptions.UserNotFountException;
import by.zemich.videohosting.service.converters.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceFacade {
    private final UserService userService;
    private final ChannelService channelService;

    public Mono<User> subscribeUserToChannel(UUID userId, UUID channelId) {

        Mono<User> userMono = userService.findById(userId)
                .switchIfEmpty(Mono.error(
                        new UserNotFountException("User with id = %s is nowhere to be found".formatted(userId))));
        Mono<Channel> channelMono = channelService.findById(channelId)
                .switchIfEmpty(Mono.error(
                        new UserNotFountException("User with id = %s is nowhere to be found".formatted(userId))));

        return channelMono.zipWith(userMono, (channel, user) -> {
                    user.subscribe(channel);
                    return user;
                })
                .flatMap(userService::save)
                .doOnSuccess(savedUser-> log.info("User was subscribed to channel", savedUser))
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to subscribeUserToChannel user to channel to user", e)));
    }

    public Flux<String> getChannelList(UUID userId) {
        return userService.findById(userId).switchIfEmpty(Mono.error(
                        new UserNotFountException("User with uuid %s is nowhere to be found".formatted(userId))
                ))
                .flatMapMany(user -> Flux.fromIterable(user.getSubscriptions()))
                .map(Channel::getTitle);

    }

    public Mono<UserCreated> create(UserCreate userCreate){
        User user = UserMapper.INSTANCE.userCreateToUser(userCreate);
        return userService.save(user).flatMap(savedUser-> Mono.just(UserMapper.INSTANCE.userToUserCreated(savedUser)));
    }

}
