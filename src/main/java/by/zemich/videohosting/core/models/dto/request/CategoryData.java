package by.zemich.videohosting.core.models.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryData {
    @NotBlank
    private String name;
}
