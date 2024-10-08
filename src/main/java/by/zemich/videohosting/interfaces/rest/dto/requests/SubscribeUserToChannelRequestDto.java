package by.zemich.videohosting.interfaces.rest.dto.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class SubscribeUserToChannelRequestDto {
    private UUID channelId;
}
