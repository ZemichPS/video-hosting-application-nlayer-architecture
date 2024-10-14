package by.zemich.videohosting.service.resthandlers;

import by.zemich.videohosting.core.models.dto.request.ChannelData;
import by.zemich.videohosting.core.models.dto.response.ChannelFullRepresentation;
import by.zemich.videohosting.core.models.dto.response.ChannelPageContent;
import by.zemich.videohosting.core.models.dto.response.SubscriberShortRepresentation;
import by.zemich.videohosting.dao.entities.Channel;
import by.zemich.videohosting.service.ChannelServiceFacade;
import by.zemich.videohosting.service.validation.Validation;
import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import static org.springframework.web.servlet.function.ServerResponse.*;

@Component
@RequiredArgsConstructor
public class ChannelHandler {
    private final ChannelServiceFacade channelServiceFacade;
    private final Validation validation;

    public ServerResponse getPage(ServerRequest request) {
        Predicate<Channel> filterPredicate = createChannelPredicateFromRequestParams(request.params());
        PageRequest pageRequest = createPageRequestFromRequestParams(request);
        Page<ChannelPageContent> page = channelServiceFacade.getPage(pageRequest, filterPredicate);
        return ok().contentType(MediaType.APPLICATION_JSON).body(page);
    }


    public ServerResponse create(ServerRequest request) throws ServletException, IOException {
        ChannelData channelData = request.body(ChannelData.class);
        validation.validate(channelData);
        ChannelFullRepresentation representation = channelServiceFacade.create(channelData);
        URI location = URI.create(request.path() + "/" + representation.getId());
        return created(location).contentType(MediaType.APPLICATION_JSON).body(representation);
    }

    public ServerResponse delete(ServerRequest request) {
        UUID channelUuid = UUID.fromString(request.pathVariable("channel_id"));
        channelServiceFacade.deleteById(channelUuid);
        return noContent().build();
    }

    public ServerResponse findById(ServerRequest request) {
        UUID channelUuid = UUID.fromString(request.pathVariable("channel_id"));
        ChannelFullRepresentation representation = channelServiceFacade.findById(channelUuid);
        return ok().contentType(MediaType.APPLICATION_JSON).body(representation);
    }

    public ServerResponse update(ServerRequest request) throws ServletException, IOException {
        ChannelData channelData = request.body(ChannelData.class);
        validation.validate(channelData);
        UUID channelId = UUID.fromString(request.pathVariable("channel_id"));
        ChannelFullRepresentation representation = channelServiceFacade.update(channelData, channelId);
        return ok().contentType(MediaType.APPLICATION_JSON).body(representation);
    }

    public ServerResponse patch(ServerRequest request) throws ServletException, IOException {
        Map<String, Object> updates = request.body(Map.class);
        UUID channelId = UUID.fromString(request.pathVariable("channel_id"));
        ChannelFullRepresentation representation = channelServiceFacade.patch(updates, channelId);
        return ok().contentType(MediaType.APPLICATION_JSON).body(representation);
    }

    public ServerResponse findSubscribers(ServerRequest request) {
        UUID channelId = UUID.fromString(request.pathVariable("channel_id"));
        List<SubscriberShortRepresentation> subscribersRepresentation = channelServiceFacade.findAllSubscribers(channelId);
        return ok().contentType(MediaType.APPLICATION_JSON).body(subscribersRepresentation);
    }

    private Predicate<Channel> createChannelPredicateFromRequestParams(MultiValueMap<String, String> params) {
        Predicate<Channel> filterPredicate = channel -> true;
        if (params.containsKey("name")) {
            filterPredicate = filterPredicate.and(channel -> channel.getName().equalsIgnoreCase(params.get("name").get(0)));
        }
        if (params.containsKey("category_id")) {
            filterPredicate = filterPredicate.and(channel -> channel.getId().equals(params.get("category_id").get(0)));
        }
        if (params.containsKey("language")) {
            filterPredicate = filterPredicate.and(channel -> channel.getName().equalsIgnoreCase(params.get("language").get(0)));
        }
        return filterPredicate;
    }

    private PageRequest createPageRequestFromRequestParams(ServerRequest request){
        Integer pageNumber = request.param("page").map(Integer::valueOf).orElse(0);
        Integer size = request.param("size").map(Integer::valueOf).orElse(10);
        Sort sort = request.param("sort")
                .map(sortParam -> Sort.by(sortParam).ascending())
                .orElse(Sort.by("name").descending());
        return PageRequest.of(pageNumber, size, sort);
    }
}
