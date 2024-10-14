package by.zemich.videohosting.dao.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
public class Channel {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    @Setter(AccessLevel.NONE)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @Setter(AccessLevel.NONE)
    private Category category;

    @Setter(AccessLevel.NONE)
    @ManyToMany(mappedBy = "subscriptions")
    private Set<User> subscribers = new HashSet<>();

    private String name;

    private String description;

    @CreationTimestamp()
    @Setter(AccessLevel.NONE)
    private LocalDateTime createdAt;

    private String language;

    private String avatar;

    public void assignCategory(Category category) {
        this.category = category;
    }

    public void addSubscriber(User user) {
        subscribers.add(user);
    }

    public void removeSubscriber(User user) {
        subscribers.remove(user);
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
