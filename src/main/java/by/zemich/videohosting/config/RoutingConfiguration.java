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

        return RouterFunctions.nest(
                RequestPredicates.path("api/v1").and(RequestPredicates.accept(APPLICATION_JSON)),
                RouterFunctions.route()
                        .add(usersRoute(userRestHandler))
                        .add(categoryRoute(categoryHandler))
                        .add(channelRoute(channelHandler))
                        .onError(Throwable.class, exceptionHandlerHolder::handle)
                        .build());
    }

    RouterFunction<ServerResponse> usersRoute(UserRestHandler handler) {
        return RouterFunctions.nest(
                RequestPredicates.path("/users"), RouterFunctions.route()
                        .nest(RequestPredicates.path("/{user_id}/channels/{channel_id}"),
                                r1 -> r1
                                        .POST(handler::subscribe)
                                        .DELETE(handler::unsubscribe)
                                        .build()
                        )
                        .nest(RequestPredicates.path("/{user_id}/channels"),
                                r2 -> r2
                                        .GET( handler::findAll)
                                        .build()
                        )
                        .nest(RequestPredicates.path("/{user_id}"),
                                r3 -> r3
                                        .GET(handler::findById)
                                        .PUT(handler::update)
                                        .PATCH(handler::patch)
                                        .DELETE(handler::delete)
                                        .build()
                        )
                        .POST(handler::create)
                        .build()
        );
    }

    RouterFunction<ServerResponse> categoryRoute(CategoryHandler handler) {
        return RouterFunctions.nest(
                RequestPredicates.path("/category"), RouterFunctions.route()
                        .nest(RequestPredicates.path("/{category_id}"),
                                r1 -> r1
                                        .GET(handler::findById)
                                        .PUT(handler::update)
                                        .PATCH(handler::patch)
                                        .DELETE(handler::delete)
                                        .build()
                        )
                        .POST(handler::create)
                        .build()
        );
    }

    RouterFunction<ServerResponse> channelRoute(ChannelHandler handler) {
        return RouterFunctions.nest(
                RequestPredicates.path("/channels"), RouterFunctions.route()
                        .GET("/{channel_id}/subscribers", handler::findSubscribers)
                        .nest(
                                RequestPredicates.path("/{channel_id}"),
                                r1 -> r1.GET(handler::findById)
                                        .PUT(handler::update)
                                        .PATCH(handler::patch)
                                        .DELETE(handler::delete)
                                        .build()
                        )
                        .POST(handler::create)
                        .GET(handler::getPage)
                        .build()
        );
    }

}
