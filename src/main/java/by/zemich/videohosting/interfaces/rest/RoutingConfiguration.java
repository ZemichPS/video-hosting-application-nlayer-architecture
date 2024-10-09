package by.zemich.videohosting.interfaces.rest;

import by.zemich.videohosting.interfaces.rest.handlers.CategoryHandler;
import by.zemich.videohosting.interfaces.rest.handlers.UserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@EnableWebFlux
@RequiredArgsConstructor
public class RoutingConfiguration {

    private final UserHandler userHandler;

    @Bean
    RouterFunction<ServerResponse> poute(UserHandler handler) {
        RouterFunction<ServerResponse> route = RouterFunctions.route()
                .path("api/v1/users", builder -> builder
                        .POST(RequestPredicates.accept(APPLICATION_JSON), userHandler::create)



                ).build();

        return route;
    }


}
