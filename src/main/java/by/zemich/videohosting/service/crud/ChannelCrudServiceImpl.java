package by.zemich.videohosting.service.crud;

import by.zemich.videohosting.dao.entities.Channel;
import by.zemich.videohosting.dao.repositories.ChannelRepository;
import by.zemich.videohosting.service.api.ChannelCrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChannelCrudServiceImpl implements ChannelCrudService {

    private final ChannelRepository channelRepository;

    @Override
    public Optional<Channel> findById(UUID id) {
        return channelRepository.findById(id);
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel save(Channel entity) {
        return channelRepository.save(entity);
    }

    @Override
    public void deleteById(UUID id) {
        channelRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return channelRepository.existsById(id);
    }


    @Override
    public Page<Channel> findAll(Pageable pageable) {
        return channelRepository.findAll(pageable);
    }
}
