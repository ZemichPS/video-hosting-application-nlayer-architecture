package by.zemich.videohosting.core.models.dto.response;

import lombok.Data;

@Data
public class ChannelPageContent {
    private String name;
    private long subscribersCount;
    private String lang;
    private String avatar;
    private CategoryShortRepresentation category;
}