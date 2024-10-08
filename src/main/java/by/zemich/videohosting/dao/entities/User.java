package by.zemich.videohosting.dao.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.*;

@Entity
@Getter
@Setter
public class User {
    @Setter(AccessLevel.NONE)
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;
    private String username;
    private String name;
    private String mail;

    @Setter(AccessLevel.NONE)
    @OrderBy("title DESC")
    @ManyToMany(
            cascade = {CascadeType.MERGE, CascadeType.PERSIST},
            mappedBy = "subscriptions"
    )
    @JoinTable(
            name = "user_channel",
            joinColumns = @JoinColumn(name = "id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "channel_id")
    )
    private Set<Channel> subscriptions = new HashSet<>();

    public void subscribe(Channel channel) {
        subscriptions.add(channel);
        channel.addSubscriber(this);
    }

}
