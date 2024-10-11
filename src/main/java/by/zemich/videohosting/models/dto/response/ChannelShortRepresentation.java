package by.zemich.videohosting.models.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class ChannelShortRepresentation {
    private UUID id;
    private String name;
}
