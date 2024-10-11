package by.zemich.videohosting.interfaces.rest.handlers;

import by.zemich.videohosting.interfaces.rest.dto.requests.SubscribeUserToChannelRequestDto;
import by.zemich.videohosting.models.dto.request.UserData;
import by.zemich.videohosting.service.UserCrudService;
import by.zemich.videohosting.service.UserServiceFacade;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserHandler {

    private final UserServiceFacade userServiceFacade;
    private final Validator validator;
    private final UserCrudService userCrudService;

    public Mono<ServerResponse> subscribe(ServerRequest request) {
        UUID userId = UUID.fromString(request.pathVariable("user_id"));
        UUID channelId = UUID.fromString(request.pathVariable("channel_id"));

        Mono<SubscribeUserToChannelRequestDto> requestDtoMono
                = request.bodyToMono(SubscribeUserToChannelRequestDto.class);

        return userServiceFacade.subscribeUserToChannel(userId,channelId)
                .flatMap(category -> ok().contentType(MediaType.APPLICATION_JSON).bodyValue(Mono.just(category)))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(Mono.just(e.getMessage())));
    }

    public Mono<ServerResponse> unsubscribe(ServerRequest request) {
        UUID userId = UUID.fromString(request.pathVariable("user_id"));
        UUID channelId = UUID.fromString(request.pathVariable("channel_id"));

        Mono<SubscribeUserToChannelRequestDto> requestDtoMono
                = request.bodyToMono(SubscribeUserToChannelRequestDto.class);

        return userServiceFacade.unsubscribeFromChannel(userId,channelId)
                .then(Mono.defer(() -> noContent().build()))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(Mono.just(e.getMessage())));
    }


    public Mono<ServerResponse> create(ServerRequest request) {

        return request.bodyToMono(UserData.class)
                .flatMap(userData -> {
                    Set<ConstraintViolation<UserData>> violations = validator.validate(userData);
                    if (!violations.isEmpty()) {
                        String message = "Validation failed" + violations.stream()
                                .map(ConstraintViolation::getMessage)
                                .collect(Collectors.joining("\n"));
                        return Mono.error(new ValidationException(message));
                    } else {
                        return Mono.just(userData);
                    }
                })
                .flatMap(userServiceFacade::create)
                .flatMap(userCreated -> {
                    URI location = URI.create("api/v1/users/" + userCreated.getId());
                    return created(location).contentType(MediaType.APPLICATION_JSON).bodyValue(Mono.just(userCreated));
                })
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(Mono.just(e.getMessage())));
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        UUID userUuid = UUID.fromString(request.pathVariable("user_id"));
        return userServiceFacade.findById(userUuid)
                .flatMap(user -> ok().contentType(MediaType.APPLICATION_JSON).bodyValue(Mono.just(user)))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(Mono.just(e.getMessage())));
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        UUID userUuid = UUID.fromString(request.pathVariable("user_id"));
        return request.bodyToMono(UserData.class)
                .flatMap(userUpdate -> {
                    Set<ConstraintViolation<UserData>> violations = validator.validate(userUpdate);
                    if (!violations.isEmpty()) {
                        String message = "Validation failed" + violations.stream()
                                .map(ConstraintViolation::getMessage)
                                .collect(Collectors.joining("\n"));
                        return Mono.error(new ValidationException(message));
                    } else {
                        return Mono.just(userUpdate);
                    }
                })
                .flatMap(userUpdate -> userServiceFacade.updateById(userUpdate, userUuid))
                .flatMap(userRepresentation -> ok().contentType(MediaType.APPLICATION_JSON).bodyValue(Mono.just(userRepresentation)))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(Mono.just(e.getMessage())));
    }

    public Mono<ServerResponse> patch(ServerRequest request) {
        UUID userUuid = UUID.fromString(request.pathVariable("user_id"));
        return request.bodyToMono(Map.class)
                .map(valueMap -> userServiceFacade.updateById(valueMap, userUuid))
                .flatMap(userRepresentation -> ok().contentType(MediaType.APPLICATION_JSON).bodyValue(Mono.just(userRepresentation)))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(Mono.just(e.getMessage())));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        UUID userUuid = UUID.fromString(request.pathVariable("user_id"));
        return userCrudService.deleteById(userUuid)
                .then(Mono.defer(() -> noContent().build()));
    }

    public Mono<ServerResponse> findAllChannels(ServerRequest request) {
        UUID userUuid = UUID.fromString(request.pathVariable("user_id"));
        return userServiceFacade.findAllChannelsById(userUuid)
                .collectList()
                .flatMap(channelsList -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(channelsList)
                )
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

}
