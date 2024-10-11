package by.zemich.videohosting.config;

import by.zemich.videohosting.interfaces.rest.handlers.UserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@EnableWebFlux
@RequiredArgsConstructor
public class RoutingConfiguration {

    private final UserHandler userHandler;

    @Bean
    RouterFunction<ServerResponse> route(UserHandler handler) {
        RouterFunction<ServerResponse> route = RouterFunctions.route()
                .path("api/v1/users", builder -> builder
                        .POST(RequestPredicates.accept(APPLICATION_JSON), userHandler::create)
                        .GET("/{user_id}", RequestPredicates.accept(APPLICATION_JSON), handler::findById)
                        .PUT("/{user_id}", RequestPredicates.accept(APPLICATION_JSON), handler::findById)
                        .DELETE("/{user_id}", RequestPredicates.accept(APPLICATION_JSON), handler::delete)
                        .GET("/{user_id}/channels", RequestPredicates.accept(APPLICATION_JSON), handler::findAllChannels)
                        .POST("/{user_id}/channels/{channel_id}", RequestPredicates.accept(APPLICATION_JSON), handler::subscribe)
                        .DELETE("/{user_id}/channels/{channel_id}", RequestPredicates.accept(APPLICATION_JSON), handler::unsubscribe)
                ).build();
        return route;
    }


}
