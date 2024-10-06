package by.zemich.videohosting.interfaces.rest;

import by.zemich.videohosting.interfaces.rest.handlers.CategoryHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@EnableWebFlux
public class RoutingConfiguration {

    @Bean
    RouterFunction<ServerResponse> categoryRouteFunction(CategoryHandler handler) {
        RouterFunction<ServerResponse> route = route()
                .path("api/v1/category", b1 -> b1.nest(
                        accept(APPLICATION_JSON), b2-> b2.GET("/{id}", handler::getCategoryByUuid)
                ))
                .build();

        return route;
    }


}
