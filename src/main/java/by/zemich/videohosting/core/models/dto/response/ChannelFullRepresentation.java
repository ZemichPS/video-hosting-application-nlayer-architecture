package by.zemich.videohosting.core.models.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Data
public class ChannelFullRepresentation {
    private UUID id;
    private String name;
    private AuthorId authorId;
    private String description;
    private LocalDateTime creationDate;
    private String language;
    private String avatar;
    private CategoryShortRepresentation category;
}
