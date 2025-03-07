package ru.practicum.shareit.request;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.User;

import java.sql.Timestamp;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "item_requests")
public class ItemRequest {

    @Id
    Long id;
    String description;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User requestor;
    Timestamp created;
}
