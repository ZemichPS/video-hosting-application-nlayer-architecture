package by.zemich.videohosting.interfaces.rest.dto;

import by.zemich.videohosting.dao.entities.Channel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class CategoryDTO {
    private UUID id;
    private String name;
    private List<Channel> channels = new ArrayList<>();
}
