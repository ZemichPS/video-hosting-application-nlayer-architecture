package by.zemich.videohosting.service.resthandlers;

import by.zemich.videohosting.core.models.dto.request.CategoryData;
import by.zemich.videohosting.core.models.dto.response.CategoryShortRepresentation;
import by.zemich.videohosting.service.CategoryServiceFacade;
import by.zemich.videohosting.service.validation.Validation;
import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.UUID;

import static org.springframework.web.servlet.function.ServerResponse.created;

@Component
@RequiredArgsConstructor
public class CategoryHandler {

    private final CategoryServiceFacade categoryServiceFacade;
    private final Validation validation;

    public ServerResponse create(ServerRequest request) throws ServletException, IOException {
        CategoryData categoryData = request.body(CategoryData.class);
        validation.validate(categoryData);
        CategoryShortRepresentation representation = categoryServiceFacade.create(categoryData);
        URI location = URI.create(request.path() + "/" + representation.getId());
        return created(location).contentType(MediaType.APPLICATION_JSON).body(representation);
    }

    public ServerResponse update(ServerRequest request) throws ServletException, IOException {
        UUID categoryId = UUID.fromString(request.pathVariable("category_id"));
        CategoryData categoryData = request.body(CategoryData.class);
        validation.validate(categoryData);
        CategoryShortRepresentation representation = categoryServiceFacade.update(categoryId, categoryData);
        return ServerResponse.ok().body(representation);
    }

    public ServerResponse patch(ServerRequest request) throws ServletException, IOException {
        UUID categoryId = UUID.fromString(request.pathVariable("category_id"));
        Map<String, Object> valuesMap = request.body(Map.class);
        CategoryShortRepresentation representation = categoryServiceFacade.patch(categoryId, valuesMap);
        return ServerResponse.ok().body(representation);
    }

    public ServerResponse findById(ServerRequest request) {
        UUID categoryId = UUID.fromString(request.pathVariable("category_id"));
        CategoryShortRepresentation representation = categoryServiceFacade.findById(categoryId);
        return ServerResponse.ok().body(representation);
    }

    public ServerResponse delete(ServerRequest request) {
        UUID categoryId = UUID.fromString(request.pathVariable("category_id"));
        categoryServiceFacade.delete(categoryId);
        return ServerResponse.noContent().build();
    }

}
