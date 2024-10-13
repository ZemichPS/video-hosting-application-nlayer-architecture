package by.zemich.videohosting.config;

import by.zemich.videohosting.service.exceptionhandlers.ExceptionHandlerHolder;
import by.zemich.videohosting.service.resthandlers.CategoryHandler;
import by.zemich.videohosting.service.resthandlers.ChannelHandler;
import by.zemich.videohosting.service.resthandlers.UserRestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;


import static org.springframework.http.MediaType.APPLICATION_JSON;

@Configuration
@RequiredArgsConstructor
public class RoutingConfiguration {

    private final ExceptionHandlerHolder exceptionHandlerHolder;

    @Bean
    RouterFunction<ServerResponse> route(UserRestHandler userRestHandler,
                                         CategoryHandler categoryHandler,
                                         ChannelHandler channelHandler) {
        return RouterFunctions.route()
                .add(usersRoute(userRestHandler))
                .add(categoryRoute(categoryHandler))
                .add(channelRoute(channelHandler))
                .onError(Throwable.class, (throwable, request) -> exceptionHandlerHolder.handle(throwable))
                .build();
    }

    RouterFunction<ServerResponse> usersRoute(UserRestHandler handler) {
        RouterFunction<ServerResponse> route = RouterFunctions.route()
                .path("api/v1/users", builder1 -> builder1
                        .POST(RequestPredicates.accept(APPLICATION_JSON), handler::create)
                        .GET("/{user_id}", handler::findById)
                        .PUT("/{user_id}", RequestPredicates.accept(APPLICATION_JSON), handler::update)
                        .PATCH("/{user_id}", RequestPredicates.accept(APPLICATION_JSON), handler::patch)
                        .DELETE("/{user_id}", handler::delete)
                        .GET("/{user_id}/channels", handler::findAll)
                        .POST("/{user_id}/channels/{channel_id}", handler::subscribe)
                        .DELETE("/{user_id}/channels/{channel_id}", handler::unsubscribe)
                )
                .build();
        return route;
    }

    RouterFunction<ServerResponse> categoryRoute(CategoryHandler handler) {
        RouterFunction<ServerResponse> route = RouterFunctions.route()
                .path("api/v1/categories", builder1 -> builder1
                        .POST(RequestPredicates.accept(APPLICATION_JSON), handler::create)
                        .GET("/{category_id}", handler::findById)
                        .PUT("/{category_id}", RequestPredicates.accept(APPLICATION_JSON), handler::update)
                        .PATCH("/{category_id}", RequestPredicates.accept(APPLICATION_JSON), handler::patch)
                        .DELETE("/{category_id}", handler::delete)
                )
                .build();
        return route;
    }

    RouterFunction<ServerResponse> channelRoute(ChannelHandler handler) {
        RouterFunction<ServerResponse> route = RouterFunctions.nest(RequestPredicates.path("api/v1/channels"),
                        RouterFunctions.route()
                                .POST(RequestPredicates.accept(APPLICATION_JSON), handler::create)
                                .GET( handler::getPage)
                                .GET("/{category_id}", handler::findById)
                                .PUT("/{category_id}", RequestPredicates.accept(APPLICATION_JSON), handler::update)
                                .PATCH("/{category_id}", RequestPredicates.accept(APPLICATION_JSON), handler::patch)
                                .DELETE("/{channel_id}", handler::delete)
                                .GET("/{channel_id}/subscribers", handler::findSubscribers)
                                .build()
                );
        return route;
    }




}
