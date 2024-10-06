package by.zemich.videohosting.dao.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.repository.cdi.Eager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Category {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    private String name;

    @OneToMany(
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            mappedBy = "category"
    )
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private List<Channel> channels = new ArrayList<>();

    public void addChannel(Channel channel) {
        channels.add(channel);
        channel.assignCategory(this);
    }

    public void removeChannel(Channel channel) {
        channel.assignCategory(null);
        channels.remove(channel);
    }

    public void removeChannels() {
        for (Channel channel : channels) {
            channel.assignCategory(null);
            channels.remove(channel);
        }
    }
}
