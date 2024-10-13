package by.zemich.videohosting.service.api;

import by.zemich.videohosting.dao.entities.Channel;
import by.zemich.videohosting.dao.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ChannelCrudService extends CrudService<Channel, UUID> {
    Page<Channel> findAll(Pageable pageable);
}
