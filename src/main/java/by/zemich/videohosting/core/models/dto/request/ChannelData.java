package by.zemich.videohosting.core.models.dto.request;

import by.zemich.videohosting.core.annotations.ValidBase64;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Base64;
import java.util.UUID;

@Data
public class ChannelData {
    @org.hibernate.validator.constraints.UUID
    private UUID authorId;
    @org.hibernate.validator.constraints.UUID
    private UUID categoryId;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    private String language;
    @ValidBase64
    private String avatar;
}
