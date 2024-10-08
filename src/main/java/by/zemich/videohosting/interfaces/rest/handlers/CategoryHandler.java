package by.zemich.videohosting.interfaces.rest.handlers;

import by.zemich.videohosting.dao.entities.Category;
import by.zemich.videohosting.service.CategoryService;
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
public class CategoryHandler {

    private final CategoryService categoryService;

    public Mono<ServerResponse> getCategoryByUuid(ServerRequest request) {
        UUID uuid = UUID.fromString(request.pathVariable("id"));
        return categoryService.findById(uuid)
                .flatMap(category -> ok().contentType(MediaType.APPLICATION_JSON).bodyValue(Mono.just(category)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
