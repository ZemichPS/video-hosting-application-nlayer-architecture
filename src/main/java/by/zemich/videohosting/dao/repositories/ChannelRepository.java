package by.zemich.videohosting.dao.repositories;

import by.zemich.videohosting.dao.entities.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, UUID>, PagingAndSortingRepository<Channel, UUID> {
}
