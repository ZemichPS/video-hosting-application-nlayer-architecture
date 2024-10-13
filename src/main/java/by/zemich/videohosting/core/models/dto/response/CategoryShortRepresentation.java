package by.zemich.videohosting.core.models.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CategoryShortRepresentation {
    private UUID id;
    private String name;
}
