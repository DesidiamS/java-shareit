package ru.practicum.shareit.request.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.User;

import java.sql.Timestamp;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "item_requests")
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "request_seq")
    @SequenceGenerator(name = "request_seq", sequenceName = "item_requests_id_seq")
    Long id;
    @Size(max = 250)
    String description;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User requestor;
    Timestamp created;

    public ItemRequest(Long id) {
        this.id = id;
    }
}
