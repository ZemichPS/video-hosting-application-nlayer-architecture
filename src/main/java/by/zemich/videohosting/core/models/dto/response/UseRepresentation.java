package by.zemich.videohosting.core.models.dto.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class UseRepresentation {
    @org.hibernate.validator.constraints.UUID
    private UUID id;
    @NotBlank
    private String username;
    @NotBlank
    private String name;
    @Email
    private String email;
}
