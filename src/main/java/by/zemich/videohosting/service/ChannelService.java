package by.zemich.videohosting.service;

import by.zemich.videohosting.dao.entities.Channel;
import by.zemich.videohosting.dao.repositories.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChannelService {
    private final ChannelRepository channelRepository;

    public Mono<Channel> findById(UUID channelId) {
        return channelRepository.findById(channelId);
    }


}
