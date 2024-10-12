package by.zemich.videohosting.service.resthandlers;

import by.zemich.videohosting.core.models.dto.request.UserData;
import by.zemich.videohosting.core.models.dto.response.ChannelShortRepresentation;
import by.zemich.videohosting.core.models.dto.response.UseRepresentation;
import by.zemich.videohosting.core.exceptions.ChannelNotFoundException;
import by.zemich.videohosting.core.exceptions.UserNotFoundException;
import by.zemich.videohosting.service.UserServiceFacade;
import by.zemich.videohosting.service.validation.Validation;
import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.IOException;
import java.net.URI;
import java.util.*;

import static org.springframework.web.servlet.function.ServerResponse.*;


@Component
@RequiredArgsConstructor
@Slf4j
public class UserRestHandler {

    private final UserServiceFacade userServiceFacade;
    private final Validation validation;

    public ServerResponse subscribe(ServerRequest request) {
        UUID userId = UUID.fromString(request.pathVariable("user_id"));
        UUID channelId = UUID.fromString(request.pathVariable("channel_id"));
        UseRepresentation representation = userServiceFacade.subscribeUserToChannel(userId, channelId);
        return ok().contentType(MediaType.APPLICATION_JSON).body(representation);
    }

    public ServerResponse unsubscribe(ServerRequest request) {
        UUID userId = UUID.fromString(request.pathVariable("user_id"));
        UUID channelId = UUID.fromString(request.pathVariable("channel_id"));
        userServiceFacade.unsubscribeFromChannel(userId, channelId);
        return noContent().build();
    }

    public ServerResponse create(ServerRequest request) throws ServletException, IOException {
        UserData userData = request.body(UserData.class);
        validation.validate(userData);
        UseRepresentation representation = userServiceFacade.create(userData);
        URI location = URI.create(request.path() + "/" + representation.getId());
        return created(location).contentType(MediaType.APPLICATION_JSON).body(representation);
    }

    public ServerResponse findById(ServerRequest request) {
        UUID userUuid = UUID.fromString(request.pathVariable("user_id"));
        try {
            UseRepresentation representation = userServiceFacade.findById(userUuid);
            return ok().contentType(MediaType.APPLICATION_JSON).body(representation);
        } catch (UserNotFoundException | ChannelNotFoundException e) {
            return ServerResponse.notFound().build();
        }
    }

    public ServerResponse update(ServerRequest request) throws ServletException, IOException {
            UUID userUuid = UUID.fromString(request.pathVariable("user_id"));
            UserData userData = request.body(UserData.class);
            validation.validate(userData);
            UseRepresentation representation = userServiceFacade.updateById(userData, userUuid);
            return ok().contentType(MediaType.APPLICATION_JSON).body(representation);
    }

    public ServerResponse patch(ServerRequest request) throws ServletException, IOException {
            UUID userUuid = UUID.fromString(request.pathVariable("user_id"));
            Map<String, Object> valuesMap = request.body(Map.class);
            UseRepresentation representation = userServiceFacade.updateById(valuesMap, userUuid);
            return ok().contentType(MediaType.APPLICATION_JSON).body(representation);
    }

    public ServerResponse delete(ServerRequest request) {
        try {
            UUID userUuid = UUID.fromString(request.pathVariable("user_id"));
            userServiceFacade.deleteById(userUuid);
            return noContent().build();
        } catch (UserNotFoundException e) {
            return ServerResponse.notFound().build();
        }
    }

    public ServerResponse findAll(ServerRequest request) {
            UUID userUuid = UUID.fromString(request.pathVariable("user_id"));
            final List<ChannelShortRepresentation> allChannels = userServiceFacade.findAllChannelsById(userUuid);
            return ok().body(allChannels);
    }
}
