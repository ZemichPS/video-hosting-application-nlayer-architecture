package by.zemich.videohosting.core.models.dto.request;

import by.zemich.videohosting.core.annotations.ValidBase64;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class ChannelData {
    private UUID authorId;
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
