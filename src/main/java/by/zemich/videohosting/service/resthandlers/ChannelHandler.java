package by.zemich.videohosting.service.resthandlers;

import by.zemich.videohosting.core.exceptions.UserNotFoundException;
import by.zemich.videohosting.core.models.dto.request.ChannelData;
import by.zemich.videohosting.core.models.dto.response.ChannelFullRepresentation;
import by.zemich.videohosting.service.ChannelServiceFacade;
import by.zemich.videohosting.service.validation.Validation;
import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

import static org.springframework.web.servlet.function.ServerResponse.created;
import static org.springframework.web.servlet.function.ServerResponse.noContent;

@Component
@RequiredArgsConstructor
public class ChannelHandler {
    private final ChannelServiceFacade channelServiceFacade;
    private final Validation validation;

    public ServerResponse create(ServerRequest request) throws ServletException, IOException {
        ChannelData channelData = request.body(ChannelData.class);
     //   validation.validate(channelData);
        ChannelFullRepresentation representation = channelServiceFacade.create(channelData);
        URI location = URI.create(request.path() + "/" + representation.getId());
        return created(location).contentType(MediaType.APPLICATION_JSON).body(representation);
    }

    public ServerResponse delete(ServerRequest request) {
            UUID channelUuid = UUID.fromString(request.pathVariable("channel_id"));
            channelServiceFacade.deleteById(channelUuid);
            return noContent().build();
    }
}
