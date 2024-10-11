package by.zemich.videohosting.service.mappers;

import by.zemich.videohosting.dao.entities.Channel;
import by.zemich.videohosting.models.dto.response.ChannelShortRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ChannelMapper {
    ChannelMapper INSTANCE = Mappers.getMapper(ChannelMapper.class);

    ChannelShortRepresentation channelToShortRepresentation(Channel channel);


}
