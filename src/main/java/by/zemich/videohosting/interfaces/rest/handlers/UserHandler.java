package by.zemich.videohosting.interfaces.rest.handlers;

import by.zemich.videohosting.dao.entities.Category;
import by.zemich.videohosting.interfaces.rest.dto.requests.SubscribeUserToChannelRequestDto;
import by.zemich.videohosting.service.CategoryService;
import by.zemich.videohosting.service.UserService;
import by.zemich.videohosting.service.UserServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
public class UserHandler {

    private UserServiceFacade userServiceFacade;

    public Mono<ServerResponse> subscribe(ServerRequest request) {
        UUID userUuid = UUID.fromString(request.pathVariable("user_id"));
        Mono<SubscribeUserToChannelRequestDto> requestDtoMono
                = request.bodyToMono(SubscribeUserToChannelRequestDto.class);

        return request.bodyToMono(SubscribeUserToChannelRequestDto.class)
                .flatMap(requestDto -> userServiceFacade.subscribe(userUuid, requestDto.getChannelId()))
                .flatMap(category -> ok().contentType(MediaType.APPLICATION_JSON).bodyValue(Mono.just(category)))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(Mono.just(e.getMessage())));
    }

    public Mono<ServerResponse> unsubscribe(ServerRequest request) {
        return null;
    }
}
