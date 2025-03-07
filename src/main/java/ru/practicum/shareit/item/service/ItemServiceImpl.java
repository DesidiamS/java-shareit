package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithComment;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static ru.practicum.shareit.item.mappers.CommentMapper.toCommentDto;
import static ru.practicum.shareit.item.mappers.ItemMapper.toItemWithComment;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public Item createItem(User user, ItemDto itemDto) {

        Item item = ItemMapper.dtoToItem(user, itemDto);

        userRepository.findById(user.getId()).orElseThrow(() -> new NotFoundException("Пользователь не найден!"));

        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(User user, Long itemId, ItemDto itemDto) {
        Item existingItem = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь не найдена!"));

        if (!existingItem.getOwner().equals(user)) {
            throw new NotFoundException("Пользователь не является владельцем вещи!");
        }

        Item item = ItemMapper.dtoToItem(user, itemDto);

        item.setId(itemId);

        itemRepository.patchItem(item.getName(), item.getDescription(), item.getAvailable(), item.getId());

        return item;
    }

    @Override
    public Collection<ItemWithComment> getAllItemsByUserId(User user) {
        Collection<ItemWithComment> result = new HashSet<>();
        for (Item item : itemRepository.findAllByOwnerId(user.getId())) {
            List<Comment> comments = commentRepository.findAllByItemId(item.getId());
            Timestamp lastBooking = bookingRepository.findLastBookingByItem(item.getId());
            Timestamp nextBooking = bookingRepository.findNextBookingByItem(item.getId());
            result.add(toItemWithComment(item, comments, lastBooking, nextBooking));
        }
        return result;
    }

    @Override
    public ItemWithComment getItemById(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Вещь не найдена!"));
        List<Comment> comments = commentRepository.findAllByItemId(item.getId());
        Timestamp lastBooking = bookingRepository.findLastBookingByItem(item.getId());
        Timestamp nextBooking = bookingRepository.findNextBookingByItem(item.getId());
        return toItemWithComment(item, comments, lastBooking, nextBooking);
    }

    @Override
    public Collection<Item> searchItem(String searchString) {
        if (searchString.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return itemRepository.findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchString);
    }

    @Override
    public CommentDto makeComment(CommentRequest request, Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь не найдена!"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден!"));

        Timestamp now = Timestamp.valueOf(LocalDate.now().atStartOfDay());

        Booking booking = bookingRepository.findBookingByItem(item).orElseThrow(()
                -> new NotFoundException("Бронирование не найдено!"));

        for (Comment comment : commentRepository.findAllByItemId(itemId)) {
            if (comment.getUser().getId().equals(userId)) {
                throw new ValidateException("Нельзя оставить отзыв!");
            }
        }

        if (!booking.getBooker().equals(user)) {
            throw new ValidateException("Пользователь не брал вещь в аренду!");
        }

        Comment comment = commentRepository.save(new Comment(null, request.getText(), item, user, now));

        return toCommentDto(comment);
    }
}
