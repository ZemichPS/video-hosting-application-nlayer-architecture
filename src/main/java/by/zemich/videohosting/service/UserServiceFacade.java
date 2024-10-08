package by.zemich.videohosting.service;

import by.zemich.videohosting.dao.entities.Channel;
import by.zemich.videohosting.dao.entities.User;
import by.zemich.videohosting.models.exceptions.UserNotFountException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceFacade {
    private final UserService userService;
    private final ChannelService channelService;

    public Mono<User> subscribe(UUID userId, UUID channelId) {

        Mono<User> userMono = userService.findById(userId)
                .switchIfEmpty(Mono.error(
                        new UserNotFountException("User with id = %s doesn't exist".formatted(userId))));
        Mono<Channel> channelMono = channelService.findById(channelId)
                .switchIfEmpty(Mono.error(
                        new UserNotFountException("Channel with id = %s doesn't exist".formatted(userId))));

        return channelMono.zipWith(userMono, (channel, user) -> {
                    user.subscribe(channel);
                    return user;
                })
                .flatMap(userService::save)
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to subscribe user to channel to user", e)));
    }

    public Flux<String> getChannelList(UUID userId) {
        return userService.findById(userId).switchIfEmpty(Mono.error(
                        new UserNotFountException("User with uuid %s is nowhere to be found".formatted(userId))
                ))
                .flatMapMany(user -> Flux.fromIterable(user.getSubscriptions()))
                .map(Channel::getTitle);
    }

}
