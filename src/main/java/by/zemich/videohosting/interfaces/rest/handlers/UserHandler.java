package by.zemich.videohosting.interfaces.rest.handlers;

import by.zemich.videohosting.interfaces.rest.dto.requests.SubscribeUserToChannelRequestDto;
import by.zemich.videohosting.models.dto.request.UserCreate;
import by.zemich.videohosting.service.UserServiceFacade;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.UUID;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
public class UserHandler {

    private final UserServiceFacade userServiceFacade;
    private final Validator validator;

    public Mono<ServerResponse> subscribe(ServerRequest request) {
        UUID userUuid = UUID.fromString(request.pathVariable("user_id"));
        Mono<SubscribeUserToChannelRequestDto> requestDtoMono
                = request.bodyToMono(SubscribeUserToChannelRequestDto.class);

        return request.bodyToMono(SubscribeUserToChannelRequestDto.class)
                .flatMap(requestDto -> userServiceFacade.subscribeUserToChannel(userUuid, requestDto.getChannelId()))
                .flatMap(category -> ok().contentType(MediaType.APPLICATION_JSON).bodyValue(Mono.just(category)))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(Mono.just(e.getMessage())));
    }

    public Mono<ServerResponse> unsubscribe(ServerRequest request) {
        return null;
    }

    public Mono<ServerResponse> create(ServerRequest request) {

        return request.bodyToMono(UserCreate.class)
                .flatMap(userCreate -> {
                    Set<ConstraintViolation<UserCreate>> violations = validator.validate(userCreate);
                    if (!violations.isEmpty()) {
                        return Mono.error(new ValidationException("Validation failed"));
                    } else {
                        return Mono.just(userCreate);
                    }
                })
                .flatMap(userServiceFacade::create)
                .flatMap(user -> ok().contentType(MediaType.APPLICATION_JSON).bodyValue(Mono.just(user)))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(Mono.just(e.getMessage())));
    }
}
